package br.edu.infnet.firebasegamelibrary.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface Endpoint {

    @GET("/about/stats")
    fun getFreeGames() : Call<JsonObject>
}