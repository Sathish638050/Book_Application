package com.example.book_application

import retrofit2.http.GET

interface BookInterface {
    @get : GET("books")
    var posts : retrofit2.Call<List<BookDetail?>?>?

    companion object{
        const val BASE_URL = "https://httpapibooks.mocklab.io/"
    }
}