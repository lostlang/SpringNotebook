package ru.sber.notebook


import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.sber.notebook.model.UserModel
import ru.sber.notebook.service.UserRepository

@Component
class Boot (
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
): ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        val user1 = UserModel(1, "Lang", passwordEncoder.encode("admin"))

        userRepository.save(user1)
    }
}