package ru.sber.notebook.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.security.SecureRandom


@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val secureRandom = SecureRandom()
        secureRandom.setSeed(SecureRandom.getSeed(55))
        return BCryptPasswordEncoder(12, secureRandom)
    }

}