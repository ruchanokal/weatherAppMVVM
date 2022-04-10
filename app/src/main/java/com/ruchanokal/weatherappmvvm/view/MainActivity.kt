package com.ruchanokal.weatherappmvvm.view


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.ruchanokal.weatherappmvvm.R
import com.ruchanokal.weatherappmvvm.databinding.ActivityMainBinding
import com.ruchanokal.weatherappmvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)


//        val view = binding.root
//        setContentView(view)

        sharedPreferences = getSharedPreferences("com.ruchanokal.weatherappmvvm", Context.MODE_PRIVATE)

        var cityName = sharedPreferences.getString("cityName","istanbul")

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.refreshData(cityName!!)



        binding.swipeRefreshLayout.setOnRefreshListener {

            binding.contentLayout.visibility = View.GONE
            binding.errorTryAgainText.visibility = View.GONE
            binding.progressBar.visibility = View.GONE

            var cName = sharedPreferences.getString("cityName",cityName)
            binding.editTextCityName.setText(cName)

            viewModel.refreshData(cName!!)
            binding.swipeRefreshLayout.isRefreshing = false


        }

        binding.searchButton.setOnClickListener {

            cityName = binding.editTextCityName.text.toString()

            sharedPreferences.edit().putString("cityName",cityName).apply()
            viewModel.refreshData(cityName!!)

            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it?.windowToken, 0)
        }


        binding.editTextCityName.setOnEditorActionListener { textView, i, keyEvent ->

            if (i == EditorInfo.IME_ACTION_DONE) {

                cityName = binding.editTextCityName.text.toString()

                sharedPreferences.edit().putString("cityName",cityName).apply()
                viewModel.refreshData(cityName!!)


                true
            }
            false
        }

        binding.tryAgain.setOnClickListener {

            sharedPreferences.edit().putString("cityName","istanbul").apply()
            viewModel.refreshData("istanbul")

        }

        observeLiveData()

        //API KEY -- afe47801031d56381f13443849ca86c9
        //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid=afe47801031d56381f13443849ca86c9
    }

    private fun observeLiveData() {

        viewModel.weatherData.observe(this, Observer { weatherdata ->

            weatherdata?.let {

                binding.contentLayout.visibility = View.VISIBLE
//                binding.degreeTextView.text = weatherdata.main.temp.toString() + " °C"
//                binding.countryCodeTextView.text = weatherdata.sys.country
//                binding.cityNameTextView.text = weatherdata.name
//                binding.humidityTextView.text = "% " + weatherdata.main.humidity.toString()
//                binding.windTextView.text = weatherdata.wind.speed.toString() + " km/h"
//                binding.longitudeTextView.text = weatherdata.coord.lon.toString()
//                binding.latitudeTextView.text = weatherdata.coord.lat.toString()

                binding.weatherModel = weatherdata




                Glide.with(this)
                    .load("http://openweathermap.org/img/wn/" + weatherdata.weather.get(0).icon + "@2x.png").into(binding.imgWeatherPictures)

            }
        })

        viewModel.weatherLoading.observe(this, Observer {

            if (it) {
                binding.contentLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.errorTryAgainText.visibility = View.GONE
                binding.tryAgain.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }

        })


        viewModel.weatherError.observe(this, Observer {

            if (it){

                binding.contentLayout.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.errorTryAgainText.visibility = View.VISIBLE
                binding.tryAgain.visibility = View.VISIBLE

            } else {
                binding.errorTryAgainText.visibility = View.GONE
                binding.tryAgain.visibility = View.GONE


            }

        })


    }
}