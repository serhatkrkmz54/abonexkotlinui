package com.abone.abonex.domain.model

data class Template(
    val id: Long,
    val name: String,
    val logoUrl: String?,
    val plans: List<Plan>
)