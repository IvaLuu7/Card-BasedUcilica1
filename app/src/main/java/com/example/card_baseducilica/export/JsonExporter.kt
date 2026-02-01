package com.example.card_baseducilica.export

import com.google.gson.Gson

object JsonExporter {
    private val gson = Gson()

    fun toJson(data: SetExportDto): String {
        return gson.toJson(data)
    }
}
