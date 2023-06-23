package com.ayush.mealfinder.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.ayush.mealfinder.R
import com.ayush.mealfinder.databinding.ActivityMealDetailsBinding
import com.ayush.mealfinder.model.MealX
import com.ayush.mealfinder.remote.RetrofitHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MealDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityMealDetailsBinding? = null

    private val binding: ActivityMealDetailsBinding
        get() = _binding!!

    private var listOfInstructions: MutableList<String> = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mealId: String? = intent.getStringExtra("mealId")

        if(mealId != null) {
            getMealDataById(mealId)
            binding.progressBar.visibility = View.VISIBLE
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getMealDataById(id: String) {

        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val response = RetrofitHelper.getInstance().getMealDetails(id)

            if(response.isSuccessful) {
                Log.d("response2", response.body().toString())
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    showDetails(response.body()!!.meals[0])
                }
            }
        }
    }

    private fun showDetails(data: MealX)
    {
        data.let { it ->
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
            Glide.with(this@MealDetailsActivity)
                .load(data.strMealThumb)
                .apply(requestOptions)
                .placeholder(R.drawable.loading)
                .skipMemoryCache(true)//for caching the image url in case phone is offline
                .into(binding.mealImage)

            binding.tvInstructions.text = it.strInstructions ?: " "
            binding.tvMealTitle.text = it.strMeal ?: " "

            populateList(it)
            binding.tvIngredients.text = showIngredients() ?: " "
            binding.tvLink.text = it.strYoutube ?: " "
            binding.tvLink.setOnClickListener {
                watchYoutubeVideo(binding.tvLink.text.toString())
            }
        }

    }

    private fun showIngredients(): String {
        var data = ""
        var count = 0
        for(i in 0 until listOfInstructions.size) {
            if(listOfInstructions[i] != " ") {
                data += "${count+1}. ${listOfInstructions[i]} \n"
                count++
            } else {
                break
            }
        }
        return data
    }
    private fun populateList(it: MealX) {
        listOfInstructions.add("${it.strMeasure1} ${it.strIngredient1}")
        listOfInstructions.add("${it.strMeasure2} ${it.strIngredient2}")
        listOfInstructions.add("${it.strMeasure3} ${it.strIngredient3}")
        listOfInstructions.add("${it.strMeasure4} ${it.strIngredient4}")
        listOfInstructions.add("${it.strMeasure5} ${it.strIngredient5}")
        listOfInstructions.add("${it.strMeasure6} ${it.strIngredient6}")
        listOfInstructions.add("${it.strMeasure7} ${it.strIngredient7}")
        listOfInstructions.add("${it.strMeasure8} ${it.strIngredient8}")
        listOfInstructions.add("${it.strMeasure9} ${it.strIngredient9}")
        listOfInstructions.add("${it.strMeasure10} ${it.strIngredient10}")
        listOfInstructions.add("${it.strMeasure11} ${it.strIngredient11}")
        listOfInstructions.add("${it.strMeasure12} ${it.strIngredient12}")
        listOfInstructions.add("${it.strMeasure13} ${it.strIngredient13}")
        listOfInstructions.add("${it.strMeasure14} ${it.strIngredient14}")
        listOfInstructions.add("${it.strMeasure15} ${it.strIngredient15}")
        listOfInstructions.add("${it.strMeasure16} ${it.strIngredient16}")
        listOfInstructions.add("${it.strMeasure17} ${it.strIngredient17}")
        listOfInstructions.add("${it.strMeasure18} ${it.strIngredient18}")
        listOfInstructions.add("${it.strMeasure19} ${it.strIngredient19}")
        listOfInstructions.add("${it.strMeasure20} ${it.strIngredient20}")

        Log.d("ingredients", listOfInstructions.toString())
    }

    private fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(id))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(id)
        )
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }
}