package ru.sber.notebook.model

import javax.persistence.*

@Table(name = "users")
@Entity
data class UserModel (
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    val id: Long? = null,

    @Column(name = "user_login")
    val login: String,

    @Column(name = "user_password")
    val password: String
)