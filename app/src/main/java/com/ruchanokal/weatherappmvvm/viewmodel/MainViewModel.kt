package com.ruchanokal.weatherappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruchanokal.weatherappmvvm.model.WeatherModel
import com.ruchanokal.weatherappmvvm.service.WeatherAPI
import com.ruchanokal.weatherappmvvm.service.WeatherAPIService
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {


    private val weatherApiService = WeatherAPIService()
    private val compositeDisposable  = CompositeDisposable()

    val weatherData = MutableLiveData<WeatherModel>()
    val weatherError = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()

    fun refreshData(cityName : String) {

        weatherLoading.value = true

        compositeDisposable.add(weatherApiService
            .getDataService(cityName)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                override fun onSuccess(t: WeatherModel) {

                    weatherData.value = t
                    weatherLoading.value = false
                    weatherError.value = false

                }

                override fun onError(e: Throwable) {

                    println("error: " + e)
                    weatherError.value = true
                    weatherLoading.value = false

                }

            }))

    }



}