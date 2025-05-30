package com.pnj.saku_planner.kakeibo.data.remote.dto

data class ScanResponse(
    val status: String,
    val tax: Long,
    val items: List<Item>
)

data class Item(
    val item_name: String,
    val price: Long
)
