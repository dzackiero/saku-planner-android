package com.pnj.saku_planner.kakeibo.presentation.models

import java.util.UUID

data class ScanUi(
    val itemName: String,
    val price: Long
)

data class EditableScanUi(
    val id: String = UUID.randomUUID().toString(),
    var itemName: String,
    var price: Double,
    var taxPerItem: Double,
    var selectedCategory: CategoryUi? = null,
)