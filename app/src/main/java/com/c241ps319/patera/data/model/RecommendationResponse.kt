package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val recommendationData: RecommendationData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RecommendationData(

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
