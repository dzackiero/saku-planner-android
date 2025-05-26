package com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {
    private val _items: MutableStateFlow<List<ScanUi>?> = MutableStateFlow(emptyList())
    val items: StateFlow<List<ScanUi>?> = _items

    fun loadItems(image: File) {
        viewModelScope.launch {
            scanRepository.predict(image)
                .map { response ->
                    val total = response.data?.items?.sumOf { it.price }
                    response.data?.items?.map { item ->
                        ScanUi(
                            itemName = item.itemName,
                            price = item.price,
                            totalPrice = total
                        )
                    }
                }
                .collect { scanUiList ->
                    _items.value = scanUiList
                }
        }
    }
}