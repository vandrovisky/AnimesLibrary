package com.lorena.projetodevapps.models

data class Media(
    val title: String,
    val thumbnail: String,
    val url: String,
    val video: String?,
    val shortDescription: String,
    val type: String,
    val nbEps: String, // or nbChapters for manga
    val score: String,
    val startDate: String,
    val endDate: String,
    val members: String,
    val rating: String?
)
