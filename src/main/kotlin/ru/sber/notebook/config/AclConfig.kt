package ru.sber.notebook.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.*
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.jdbc.LookupStrategy
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AclConfig: GlobalMethodSecurityConfiguration() {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun aclService(): JdbcMutableAclService {
        return JdbcMutableAclService(dataSource, lookupStrategy(), aclCache())
    }

    @Bean
    fun lookupStrategy(): LookupStrategy {
        return BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), ConsoleAuditLogger())
    }

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        val permissionEvaluator = AclPermissionEvaluator(aclService())
        expressionHandler.setPermissionEvaluator(permissionEvaluator)
        expressionHandler.setPermissionCacheOptimizer(AclPermissionCacheOptimizer(aclService()))
        return expressionHandler
    }

    @Bean
    fun aclCache(): EhCacheBasedAclCache {
        return EhCacheBasedAclCache(
            Objects.requireNonNull(aclEhCacheFactoryBean().getObject()),
            permissionGrantingStrategy(),
            aclAuthorizationStrategy()
        )
    }

    @Bean
    fun aclEhCacheFactoryBean(): EhCacheFactoryBean {
        val ehCacheFactoryBean = EhCacheFactoryBean()
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject()!!)
        ehCacheFactoryBean.setCacheName("aclCache")
        return ehCacheFactoryBean
    }

    @Bean
    fun aclCacheManager(): EhCacheManagerFactoryBean {
        return EhCacheManagerFactoryBean()
    }

    @Bean
    fun permissionGrantingStrategy(): PermissionGrantingStrategy {
        return DefaultPermissionGrantingStrategy(ConsoleAuditLogger())
    }

    @Bean
    fun aclAuthorizationStrategy(): AclAuthorizationStrategy {
        return AclAuthorizationStrategyImpl(SimpleGrantedAuthority("ROLE_EDITOR"))
    }

}