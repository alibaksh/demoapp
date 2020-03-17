package com.example.demoapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created on 2020-03-14.
 */

@Parcelize
class Media(
    val type: String?,
    val subtype: String?,
    val caption: String?,
    val copyright: String?,
    val approved_for_syndication: Number?,
    @SerializedName("media-metadata") val mediaMetaData: List<MediaMeta>?
) : Parcelable

@Parcelize
class MediaMeta(val url: String?, val format: String?, val height: Number?, val width: Number?) :
    Parcelable