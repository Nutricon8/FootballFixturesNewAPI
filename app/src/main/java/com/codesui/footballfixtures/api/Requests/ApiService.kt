package com.codesui.footballfixtures.api.Requests

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("fixtures")
    suspend fun getFixtures(
        @QueryMap params: Map<String, String>
    ): JsonArray

    @GET("fixtures/lineups")
    suspend fun getLineups(
        @QueryMap params: Map<String, String>
    ): JsonObject

    @GET("leagues")
    suspend fun getLeagues(
        @QueryMap params: Map<String, String>
    ): JsonArray

    @GET("standings")
    suspend fun getStandings(
        @QueryMap params: Map<String, String>
    ): JsonArray

    @GET("players/topscorers")
    suspend fun getTopScorers(
        @QueryMap params: Map<String, String>
    ): JsonArray

    @GET("players")
    suspend fun getPlayer(
        @QueryMap params: Map<String, String>
    ): JsonArray

    @GET("/")
    suspend fun getFixture(
        @QueryMap params: Map<String, String>
    ): JsonArray
}