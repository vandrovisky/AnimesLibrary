package com.lorena.projetodevapps

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

interface MyAnimeListApi {
    @GET("anime/top/special?p=1")
    @Headers(
        "x-rapidapi-host: myanimelist-api1.p.rapidapi.com",
        "x-rapidapi-key: 415baa74d2mshb02ada7937f18b3p1edbecjsn325a52750ce0"
    )
    suspend fun getTopSpecialAnimes(): List<Anime>

    @GET("anime/search")
    @Headers(
        "x-rapidapi-host: myanimelist-api1.p.rapidapi.com",
        "x-rapidapi-key: 415baa74d2mshb02ada7937f18b3p1edbecjsn325a52750ce0"
    )
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("n") n: Int = 10,
        @Query("score") score: Int = 5,
        @Query("genre") genre: Int = 1
    ): List<Media>

    @GET("anime/{id}")
    @Headers(
        "x-rapidapi-host: myanimelist-api1.p.rapidapi.com",
        "x-rapidapi-key: 415baa74d2mshb02ada7937f18b3p1edbecjsn32550ce0"
    )
    suspend fun getAnimeById(@Path("id") id: Int): Media

    @GET("manga/search")
    @Headers(
        "x-rapidapi-host: myanimelist-api1.p.rapidapi.com",
        "x-rapidapi-key: 415baa74d2mshb02ada7937f18b3p1edbecjsn325a52750ce0"
    )
    suspend fun searchManga(
        @Query("q") query: String,
        @Query("n") n: Int = 10,
        @Query("score") score: Int = 5,
        @Query("genre") genre: Int = 1
    ): List<Media>

    @GET("manga/{id}")
    @Headers(
        "x-rapidapi-host: myanimelist-api1.p.rapidapi.com",
        "x-rapidapi-key: 415baa74d2mshb02ada7937f18b3p1edbecjsn32550ce0"
    )
    suspend fun getMangaById(@Path("id") id: Int): Media
}

object ApiClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://myanimelist-api1.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: MyAnimeListApi = retrofit.create(MyAnimeListApi::class.java)
}
