package ru.sber.notebook.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var dataSource: DataSource

    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select user_login, user_password, 'true' from users where user_login=?")
            .authoritiesByUsernameQuery("select user_login, 'ADMIN' from users where user_login=?")
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
            .antMatchers("/app/**").hasAnyRole()
            .antMatchers("/api/**").hasAnyRole()
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll()
    }
}