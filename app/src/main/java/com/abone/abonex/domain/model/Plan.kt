package com.abone.abonex.domain.model

data class Plan(
    val id: Long,
    val name: String,
    val amount: Double,
    val currency: String
)