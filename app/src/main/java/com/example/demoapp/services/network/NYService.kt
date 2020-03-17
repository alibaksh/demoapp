package com.example.demoapp.services.network

import com.example.demoapp.constants.EnvironmentConstants.Companion.PATH_MOST_VIEWED
import com.example.demoapp.services.network.responses.ArticleResponse
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NYService {
    @GET(PATH_MOST_VIEWED)
    fun getMostViewed(@Path("section") section: String, @Path("period") period: String): Maybe<ArticleResponse>
}