package com.ruchanokal.weatherappmvvm.service

import com.ruchanokal.weatherappmvvm.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q={city name}&appid=afe47801031d56381f13443849ca86c9


interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&appid=afe47801031d56381f13443849ca86c9")
    fun getData( @Query("q") cityName : String ) : Single<WeatherModel>

}