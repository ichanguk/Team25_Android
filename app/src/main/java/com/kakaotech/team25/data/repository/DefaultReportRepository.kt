package com.kakaotech.team25.data.repository

import android.util.Log
import com.kakaotech.team25.data.network.calladapter.Result
import com.kakaotech.team25.data.network.dto.mapper.asDomain
import com.kakaotech.team25.data.remote.ReportApiService
import com.kakaotech.team25.domain.model.Report
import com.kakaotech.team25.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultReportRepository @Inject constructor(
    private val reportApiService: ReportApiService
) : ReportRepository {
    override fun getReportFlow(reservationId: String): Flow<Report> = flow {
        val result = reportApiService.getReportInfo(reservationId)
        if (result is Result.Success) result.body?.data?.let { reportDtoList ->
            Log.d("DefaultReportRepository", reportDtoList.first().toString())
            emit(reportDtoList.first().asDomain())
        }
    }
}
