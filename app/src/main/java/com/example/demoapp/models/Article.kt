package com.example.demoapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created on 2020-03-14.
 */
@Parcelize
class Article(
    val uri: String?,
    val url: String?,
    val id: Number?,
    val asset_id: Number?,
    val source: String?,
    val published_date: String?,
    val updated: String?,
    val section: String?,
    val subsection: String?,
    val nytdsection: String?,
    val adx_keywords: String?,
    val byline: String?,
    val type: String?,
    val title: String?,
    val abstract: String?,
    val des_facet: List<String>?,
    val org_facet: List<String>?,
    val per_facet: List<String>?,
    val geo_facet: List<String>?,
    val media: List<Media>?,
    val eta_id: Number?
) : Parcelable {

    fun getByLine(): String {
        return if(byline.isNullOrBlank()) "N/A" else byline

    }
}