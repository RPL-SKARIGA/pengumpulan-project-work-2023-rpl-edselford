package com.edselmustapa.mywallet.lib.model

import androidx.compose.runtime.Composable

data class Voucher(
    val id: Int,
    val name: String,
    val icon: Int,
    val type: List<VoucherType> = emptyList(),
    val input: @Composable (callback: (complete: Boolean) -> Unit) -> Unit = {}
)

data class VoucherType(
    val name: String,
    val nominal: List<Pair<String, Long>> = emptyList()
)