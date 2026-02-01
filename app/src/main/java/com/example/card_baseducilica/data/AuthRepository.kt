package com.example.card_baseducilica.data

import com.example.card_baseducilica.data.dao.UserDao
import com.example.card_baseducilica.data.entity.UserEntity
import com.example.card_baseducilica.data.util.PasswordHasher

class AuthRepository(
    private val userDao: UserDao
) {

    suspend fun login(username: String, password: String): Int {
        val u = username.trim()
        val p = password.trim()

        if (u.isBlank() || p.isBlank()) {
            throw IllegalArgumentException("Unesi korisničko ime i lozinku.")
        }


        val hashedPassword = PasswordHasher.sha256(p)

        val userId = userDao.login(u, hashedPassword)
        return userId ?: throw IllegalArgumentException("Pogrešno korisničko ime ili lozinka.")
    }

    suspend fun register(username: String, password: String, confirmPassword: String) {
        val u = username.trim()
        val p = password.trim()
        val cp = confirmPassword.trim()

        if (u.isBlank() || p.isBlank() || cp.isBlank()) {
            throw IllegalArgumentException("Sva polja su obavezna.")
        }
        if (p != cp) {
            throw IllegalArgumentException("Lozinke se ne podudaraju.")
        }


        val existing = userDao.getByUsername(u)
        if (existing != null) {
            throw IllegalArgumentException("Korisničko ime već postoji.")
        }

        val hashedPassword = PasswordHasher.sha256(p)

        userDao.insert(
            UserEntity(
                username = u,
                password = hashedPassword
            )
        )
    }
}
