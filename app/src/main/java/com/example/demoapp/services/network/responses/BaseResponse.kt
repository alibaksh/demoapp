package com.example.demoapp.services.network.responses

/**
 * Created on 2020-03-14.
 */
interface BaseResponse {
    val status: String?
    val copyright: String?
    val num_results: Number?
    val results: List<Any>?
}