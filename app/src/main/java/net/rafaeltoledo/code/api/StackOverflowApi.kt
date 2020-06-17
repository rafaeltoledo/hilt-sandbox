package net.rafaeltoledo.code.api

import retrofit2.http.GET
import retrofit2.http.Query

interface StackOverflowApi {

    @GET("2.2/users?order=desc&sort=reputation&site=stackoverflow")
    suspend fun fetchUsers(@Query("page") page: Int): Collection<User>
}