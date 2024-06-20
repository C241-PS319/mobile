package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(

	@field:SerializedName("data")
	val dataImage: DataImage? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataImage(

	@field:SerializedName("url_image")
	val urlImage: String? = null
)
