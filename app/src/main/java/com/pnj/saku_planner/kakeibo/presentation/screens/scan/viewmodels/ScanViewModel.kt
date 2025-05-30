package com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {
    private val _errorMsg: MutableStateFlow<String?> = MutableStateFlow("")
    val errorMsg: StateFlow<String?> = _errorMsg

    private val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean?> = _isLoading

    private val _items: MutableStateFlow<List<ScanUi>?> = MutableStateFlow(emptyList())
    val items: StateFlow<List<ScanUi>?> = _items

    private val _totalPrice: MutableStateFlow<String?> = MutableStateFlow("")
    val totalPrice: StateFlow<String?> = _totalPrice

    private val _tax: MutableStateFlow<String?> = MutableStateFlow("")
    val tax: StateFlow<String?> = _tax

    fun loadItems(image: File) {
        viewModelScope.launch {
            scanRepository.predict(image)
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val items = resource.data?.items ?: emptyList()
                            val total = items.sumOf { it.price }

                            val scanUiList = items.map { item ->
                                ScanUi(
                                    itemName = item.item_name,
                                    price = item.price
                                )
                            }

                            _items.value = scanUiList
                            _totalPrice.value = total.toString()
                            _tax.value = resource.data?.tax.toString()
                            Timber.d("Received success with items: ${resource.data?.items?.size}")
                        }

                        is Resource.Error -> {
                            Timber.e("Error received: ${resource.message}")
                            _errorMsg.value = resource.message ?: "Unknown error"
                        }

                        is Resource.Loading<*> -> {
                            Timber.d("Loading...")
                            _isLoading.value = true
                        }
                    }
                }
                .collect {
                    // Setelah selesai, set isLoading ke false
                    _isLoading.value = false
                }
        }
    }

}