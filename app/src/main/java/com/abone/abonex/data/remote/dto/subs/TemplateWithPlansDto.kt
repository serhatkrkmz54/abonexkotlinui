package com.abone.abonex.data.remote.dto.subs

data class TemplateWithPlansDto(
    val id: Long,
    val name: String,
    val logoUrl: String?,
    val plans: List<PlanDto>
)