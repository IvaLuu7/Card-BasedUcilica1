package com.example.card_baseducilica.data

import com.example.card_baseducilica.data.dao.UserDao
import com.example.card_baseducilica.data.entity.UserEntity

class AuthRepository(
    private val userDao: UserDao
) {

    suspend fun login(username: String, password: String): Int {
        val u = username.trim()
        val p = password.trim()

        if (u.isBlank() || p.isBlank()) {
            throw IllegalArgumentException("Unesi korisničko ime i lozinku.")
        }

        val userId = userDao.login(u, p)
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

        // ✅ provjera postoji li username
        val existing = userDao.getByUsername(u)
        if (existing != null) {
            throw IllegalArgumentException("Korisničko ime već postoji.")
        }

        userDao.insert(UserEntity(username = u, password = p))
    }
}