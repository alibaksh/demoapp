package com.example.demoapp.services.network.responses

import android.os.Parcelable
import com.example.demoapp.models.Article
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

/**
 * Created on 2020-02-24.
 */
@Parcelize
open class ArticleResponse(
    override val results: ArrayList<Article>,
    override val status: String?,
    override val copyright: String?,
    override val num_results: Number?
) : BaseResponse, Parcelable {

    companion object {
        fun getMockInstance(response: String) : ArticleResponse {
            return Gson().fromJson(response, ArticleResponse::class.java)
        }
    }
}