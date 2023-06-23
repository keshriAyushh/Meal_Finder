package com.ayush.mealfinder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.mealfinder.R
import com.ayush.mealfinder.common.MealItemClicksListener
import com.ayush.mealfinder.model.MealX
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class MealsAdapter(
    private val context: Context,
    private val mealsData: List<MealX>,
    private val listener: MealItemClicksListener
) : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false)

        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.text.text = mealsData[position].strMeal

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(context)
            .load(mealsData[position].strMealThumb)
            .apply(requestOptions)
            .placeholder(R.drawable.loading)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.image)

        holder.itemView.setOnClickListener {
            listener.onMealItemClick(it, mealsData[position].idMeal)
        }
    }

    override fun getItemCount() = mealsData.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ShapeableImageView = itemView.findViewById(R.id.meal_image)
        val text: TextView = itemView.findViewById(R.id.meal_title)
    }
}