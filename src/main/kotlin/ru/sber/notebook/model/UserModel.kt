package ru.sber.notebook.model

import javax.persistence.*

@Table(name = "user")
@Entity
data class UserModel (
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    val id: Long? = null,
    val login: String,
    val password: String
)