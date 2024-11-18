package com.example.kuit4_android_retrofit.retrofit.service

import com.example.kuit4_android_retrofit.data.CategoryData
import com.example.kuit4_android_retrofit.data.MenuData
import retrofit2.Call
import retrofit2.http.GET

interface PopularMenuService {
    @GET("popular") //() 안에 endpoint의 값 넣어서 사용
    fun getCategories(): Call<List<MenuData>>
}