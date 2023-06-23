package com.ayush.mealfinder.ui

import android.content.Intent
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayush.mealfinder.common.MealItemClicksListener
import com.ayush.mealfinder.databinding.ActivityMainBinding
import com.ayush.mealfinder.remote.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), MealItemClicksListener {

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.textInputLayout.setEndIconOnClickListener {
            if(binding.etMeal.text.toString().isNotEmpty()) {
                binding.tvError.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
                getData(binding.etMeal.text.toString())
                binding.etMeal.text = null
            }
        }
    }

    private fun getData(s: String) {
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val response = RetrofitHelper.getInstance().getMeals(s)

            if(response.isSuccessful) {

                Log.d("response", response.body().toString())

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.rvList.visibility = View.VISIBLE
                    if(response.body()?.meals == null) {
                        binding.rvList.visibility = View.GONE
                        binding.tvError.text = "No meals found with \"$s\""
                        binding.tvError.visibility = View.VISIBLE
                    } else  {
                        mealsAdapter = MealsAdapter(this@MainActivity, response.body()?.meals!!, this@MainActivity)

                        binding.rvList.apply {
                            adapter = mealsAdapter
                            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                        }
                    }

                }

            }
        }
    }

    override fun onMealItemClick(view: View, mealId: String) {
        val intent = Intent(this@MainActivity, MealDetailsActivity::class.java)
        intent.apply {
            putExtra("mealId", mealId)
        }
        startActivity(intent)
    }

}