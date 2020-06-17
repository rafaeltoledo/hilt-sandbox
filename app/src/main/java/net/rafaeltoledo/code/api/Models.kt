package net.rafaeltoledo.code.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Collection<T>(
    @Json(name = "items") val items: List<T>,
    @Json(name = "has_more") val hasMore: Boolean
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "display_name") val displayName: String,
    @Json(name = "profile_image") val profileImage: String,
    @Json(name = "location") val location: String?,
    @Json(name = "link") val link: String
)
