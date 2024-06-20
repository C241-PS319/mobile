package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class GetHistoriesResponse(

	@field:SerializedName("data")
	val data: List<History?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Recommendation(

	@field:SerializedName("healing")
	val healing: String? = null,

	@field:SerializedName("cost")
	val cost: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cause")
	val cause: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("prevention")
	val prevention: String? = null
)

data class History(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: Recommendation? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("picture")
	val picture: String? = null
)
