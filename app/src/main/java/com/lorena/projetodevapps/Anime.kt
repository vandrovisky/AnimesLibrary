package com.lorena.projetodevapps.models

data class Anime(
    val title: String,
    val picture_url: String,
    val myanimelist_url: String,
    val myanimelist_id: Int,
    val rank: Int,
    val score: Float,
    val type: String,
    val aired_on: String,
    val members: Int,
    val nbEps: Int,
    val startDate: String,
    val endDate: String
)
