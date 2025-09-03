package com.abone.abonex.data.mapper

import com.abone.abonex.data.remote.dto.subs.PlanDto
import com.abone.abonex.data.remote.dto.subs.TemplateWithPlansDto
import com.abone.abonex.domain.model.Plan
import com.abone.abonex.domain.model.Template

fun PlanDto.toPlan(): Plan {
    return Plan(
        id = id,
        name = planName,
        amount = amount,
        currency = currency
    )
}

fun TemplateWithPlansDto.toTemplate(): Template {
    return Template(
        id = id,
        name = name,
        logoUrl = logoUrl,
        plans = plans.map { it.toPlan() }
    )
}