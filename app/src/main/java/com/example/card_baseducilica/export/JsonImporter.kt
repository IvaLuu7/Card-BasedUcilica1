package com.example.card_baseducilica.export

import com.google.gson.Gson

object JsonImporter {
    private val gson = Gson()

    fun fromJson(json: String): SetExportDto {
        return gson.fromJson(json, SetExportDto::class.java)
    }
}
