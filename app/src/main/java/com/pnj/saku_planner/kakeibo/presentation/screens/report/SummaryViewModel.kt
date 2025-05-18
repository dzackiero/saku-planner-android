package com.pnj.saku_planner.kakeibo.presentation.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<SummaryState> = MutableStateFlow(SummaryState())
    val state: StateFlow<SummaryState> = _state

    val callbacks = SummaryCallback(
        onOptionSelected = { selectedType ->
            _state.value = _state.value.copy(selectedType = selectedType.lowercase())
            getSummaryData()
        },
        onTimeTypeSelected = { selectedTimeType ->
            _state.value = _state.value.copy(selectedTimeType = selectedTimeType)
            if (selectedTimeType == "monthly") {
                val startDate = LocalDate.now().withDayOfMonth(1)
                val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())
                _state.value = _state.value.copy(
                    startDate = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli(),
                    endDate = endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            } else {
                val startDate = LocalDate.now().withDayOfYear(1)
                val endDate = startDate.withDayOfYear(startDate.lengthOfYear())
                _state.value = _state.value.copy(
                    startDate = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli(),
                    endDate = endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            }
        },
        onDateRangeSelected = { startDate, endDate ->
            _state.value = _state.value.copy(startDate = startDate, endDate = endDate)
            getSummaryData()
        }
    )

    init {
        getSummaryData()
    }

    private fun getSummaryData() {
        viewModelScope.launch {
            val stateValue = _state.value
            val summaryData = when (_state.value.selectedType) {
                "expense", "income" -> transactionRepository.getTransactionSummaryByCategory(
                    TransactionType.valueOf(stateValue.selectedType.uppercase()),
                    stateValue.startDate,
                    stateValue.endDate,
                )

                "kakeibo" -> transactionRepository.getKakeiboSummary(
                    stateValue.startDate,
                    stateValue.endDate,
                )

                else -> emptyList()
            }

            _state.value = _state.value.copy(summaryData = summaryData)
        }
    }
}

data class SummaryState(
    val summaryData: List<SummaryData> = emptyList(),
    val selectedType: String = "expense",
    val selectedTimeType: String = "monthly",
    val startDate: Long = LocalDate
        .now()
        .withDayOfMonth(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli(),
    val endDate: Long = LocalDate
        .now()
        .withDayOfMonth(LocalDate.now().lengthOfMonth())
        .atTime(LocalTime.MAX)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
)

data class SummaryCallback(
    val onOptionSelected: (String) -> Unit = {},
    val onTimeTypeSelected: (String) -> Unit = {},
    val onDateRangeSelected: (Long, Long) -> Unit = { _, _ -> },
)