package com.ayush.mealfinder.remote

import com.ayush.mealfinder.model.Meal
import com.ayush.mealfinder.model.MealX
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MealService {

    @GET("api/json/v1/1/search.php")
    suspend fun getMeals(@Query("s")s: String): Response<Meal>

    @GET("api/json/v1/1/lookup.php")
    suspend fun getMealDetails(@Query("i")id: String): Response<Meal>
}