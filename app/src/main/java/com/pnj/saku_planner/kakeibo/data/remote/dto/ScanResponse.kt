package com.pnj.saku_planner.kakeibo.data.remote.dto

data class ScanResponse(
    val status: String,
    val items: List<Item>
)

data class Item(
    val itemName: String,
    val price: Int
)
