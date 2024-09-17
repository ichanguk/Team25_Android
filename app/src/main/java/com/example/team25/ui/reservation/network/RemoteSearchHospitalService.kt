package com.example.team25.ui.reservation.network

import com.example.team25.BuildConfig
import com.example.team25.domain.HospitalDomain
import com.example.team25.ui.reservation.interfaces.SearchHospitalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteSearchHospitalService: SearchHospitalService {
    private val kakaoApi: KakaoApi = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KakaoApi::class.java)

    override suspend fun getSearchedResult(keyword: String, page: Int): List<HospitalDomain> {
        val hospitals = mutableListOf<HospitalDomain>()

        val response = kakaoApi.getSearchKeyword(
            key = BuildConfig.KAKAO_REST_API_KEY,
            categoryGroupCode = "HP8",
            query = keyword,
            size = 15,
            page = page
        )

        if (response.isSuccessful) {
            response.body()?.documents?.let { documents ->
                hospitals.addAll(documents.map { document ->
                    HospitalDomain(
                        name = document.name,
                        address = document.address,
                    )
                })
            }
        } else {
            throw RuntimeException("API 요청 실패: ${response.errorBody()?.string()}")
        }
        return hospitals
    }
}
