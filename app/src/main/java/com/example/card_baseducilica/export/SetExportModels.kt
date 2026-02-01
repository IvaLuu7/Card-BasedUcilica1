package com.example.card_baseducilica.export

data class CardExportDto(
    val question: String,
    val answer: String,
    val isFavorite: Boolean
)

data class SetExportDto(
    val title: String,
    val cards: List<CardExportDto>
)
