package com.kakaotech.team25.ui.main.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25.domain.model.Report
import com.kakaotech.team25.domain.model.ReservationInfo
import com.kakaotech.team25.domain.repository.ReportRepository
import com.kakaotech.team25.domain.usecase.GetReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationCheckReportViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase
) : ViewModel() {
    private val _reservationInfo = MutableStateFlow(ReservationInfo())
    val reservationInfo: StateFlow<ReservationInfo> = _reservationInfo

    @OptIn(ExperimentalCoroutinesApi::class)
    val reportInfo: StateFlow<Report> = _reservationInfo
        .flatMapLatest { reservationInfo ->
            getReportUseCase(reservationInfo.reservationId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = Report()
        )

    fun updateReservationInfo(reservationInfo: ReservationInfo){
        viewModelScope.launch {
            _reservationInfo.value = reservationInfo
        }
    }
}
