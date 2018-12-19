package br.com.fiap.healthtrack.user.dao

import br.com.fiap.healthtrack.user.User

interface UserDao{
    fun getUserById(id: String): User
    fun addUser(user: User)
    fun updateUser(user: User)
    fun getUser(): User
    fun getUserByEmail(email: String): User
}