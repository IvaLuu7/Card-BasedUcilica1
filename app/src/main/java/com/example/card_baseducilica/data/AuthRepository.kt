package com.example.card_baseducilica.data

import com.example.card_baseducilica.data.dao.UserDao
import com.example.card_baseducilica.data.entity.UserEntity
import com.example.card_baseducilica.data.util.PasswordHasher

class AuthRepository(
    private val userDao: UserDao
) {

    suspend fun register(
        username: String,
        password: String,
        confirmPassword: String
    ) {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            throw IllegalArgumentException("Sva polja su obavezna")
        }

        if (password != confirmPassword) {
            throw IllegalArgumentException("Lozinke se ne podudaraju")
        }

        val existingUser = userDao.findByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Korisničko ime već postoji")
        }

        val passwordHash = PasswordHasher.sha256(password)

        val user = UserEntity(
            username = username,
            password = passwordHash
        )

        userDao.insert(user)
    }

    suspend fun login(username: String, password: String): UserEntity {
        if (username.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("Unesite korisničko ime i lozinku")
        }

        val user = userDao.findByUsername(username)
            ?: throw IllegalArgumentException("Pogrešno korisničko ime ili lozinka")

        val passwordHash = PasswordHasher.sha256(password)
        if (user.password != passwordHash) {
            throw IllegalArgumentException("Pogrešno korisničko ime ili lozinka")
        }

        return user
    }
}