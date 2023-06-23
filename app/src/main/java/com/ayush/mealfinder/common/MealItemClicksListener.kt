package com.ayush.mealfinder.common

import android.view.View

interface MealItemClicksListener {
    fun onMealItemClick(view: View, mealId: String)
}