package com.mit.apartmentmanagement.data.model

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("content")
    val content: List<T>,
    @SerializedName("page")
    val page: Page,
)
