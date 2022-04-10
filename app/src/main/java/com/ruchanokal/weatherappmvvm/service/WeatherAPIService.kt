package com.ruchanokal.weatherappmvvm.service

import com.ruchanokal.weatherappmvvm.model.WeatherModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


//https://api.openweathermap.org/data/2.5/weather?q={city name}&appid=afe47801031d56381f13443849ca86c9

class WeatherAPIService {

    private val BASE_URL = "https://api.openweathermap.org/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(WeatherAPI::class.java)

    fun getDataService(cityName : String) : Single<WeatherModel>{
        return api.getData(cityName)
    }

}