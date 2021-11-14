package ru.sber.notebook.service

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.notebook.model.UserModel

@Repository
interface UserRepository: CrudRepository<UserModel, Long>{

}