package com.abone.abonex.domain.repository

enum class Gender(val displayName: String) {
    MALE("Erkek"),
    FEMALE("Kadın"),
    OTHER("Diğer"),
    PREFER_NOT_TO_SAY("Belirtmek İstemiyorum")
}