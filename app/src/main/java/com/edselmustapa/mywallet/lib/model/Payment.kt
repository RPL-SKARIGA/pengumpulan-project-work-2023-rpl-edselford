package com.edselmustapa.mywallet.lib.model

data class Payment(
    val id: Int,
    val name: String,
    val enable: Boolean,
    val description: String = ""
)