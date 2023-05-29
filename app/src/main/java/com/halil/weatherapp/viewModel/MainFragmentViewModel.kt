package com.halil.weatherapp.viewModel

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.weatherapp.R
import com.halil.weatherapp.entity.DatabaseLocation
import com.halil.weatherapp.entity.SearchLocation
import com.halil.weatherapp.entity.Weather
import com.halil.weatherapp.entity.WeatherDetail
import com.halil.weatherapp.retrofit.WeatherApiServis
import com.halil.weatherapp.room.LocationDatabase
import com.halil.weatherapp.sharedPreferences.WeatherSharedPreferences
import com.halil.weatherapp.util.LocationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class MainFragmentViewModel() : ViewModel() {
    var primaryWeatherLiveData = MutableLiveData<Weather>()
    var allLocationWeathersLiveData = MutableLiveData<ArrayList<Weather>>()
    var isUpdating = MutableLiveData<Boolean>(false)
    var locationListLiveData = MutableLiveData<ArrayList<String>>(
        arrayListOf()
    )
    var currentLocationLiveData = MutableLiveData<String>()
    var weatherDetailListLiveData = MutableLiveData<ArrayList<WeatherDetail>>(arrayListOf())
    var lastestWeatherLiveData = MutableLiveData<HashMap<String, Weather>>(hashMapOf())
    var searchLocationListLiveData = MutableLiveData<ArrayList<SearchLocation>>(arrayListOf())

    var EPAIndexList = hashMapOf<Int, String>()
    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var locationData: LocationData


    public fun start(context: Context, activity: Activity, lifecycleOwner: LifecycleOwner) {
        this.context = context
        this.activity = activity
        EPAIndexList=hashMapOf<Int, String>(
            1 to context.getString(R.string.EpaIndex1),
            2 to context.getString(R.string.EpaIndex2),
            3 to context.getString(R.string.EpaIndex3),
            4 to context.getString(R.string.EpaIndex4),
            5 to context.getString(R.string.EpaIndex5),
            6 to context.getString(R.string.EpaIndex6)
        )
        getLocationFromDatabase()
        viewModelScope.launch(Dispatchers.Default) {
            getLatestWeather(true)
        }
        update()
        //allWeatherUpdate()
        locationData = LocationData(context, activity)
        if (!locationData.start()) {//izin verilmezse
            Handler().also {
                it.post(object : Runnable {
                    override fun run() {
                        if (!locationData.start()) {
                            it.postDelayed(this, 1000)
                        } else {
                            it.removeCallbacks(this)
                        }
                    }

                })
            }

        }
        locationData.getLiveGpsLocation().let {
            it.observe(lifecycleOwner, Observer {
                currentLocationLiveData.value = "${it.latitude} , ${it.longitude}"
                if (!locationListLiveData.value.isNullOrEmpty()) {
                    locationListLiveData.value?.set(0, "${it.latitude} , ${it.longitude}")
                }


            })
        }


    }


    public fun allWeatherUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                locationListLiveData.value?.let {
                    getAllWeather(it, this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    public fun update() {
        isUpdating.value = true;
        viewModelScope.launch(Dispatchers.IO) {

            try {
                getCurrentWeather(this)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "" + e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                isUpdating.value = false;
            }
        }

    }

    private suspend fun getLatestWeather(setAsCurrent: Boolean = false) {
        val weathers = WeatherSharedPreferences(context).getLatestWeather()

        weathers?.let {
            var list = hashMapOf<String, Weather>()
            it.forEach {
                list.put(it.location?.name ?: context.getString(R.string.unknownLocation), it)
            }
            withContext(Dispatchers.Main) {
                lastestWeatherLiveData.value = list
                allLocationWeathersLiveData.value = arrayListOf(*it)
                if (setAsCurrent && !it.isNullOrEmpty()) {
                    primaryWeatherLiveData.value = it[0]
                }
            }
        }
    }


    private fun saveLatestWeather(list: HashMap<String, Weather>? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            (list ?: lastestWeatherLiveData.value)?.let {
                WeatherSharedPreferences(context).saveLatestWeather(it.values.toTypedArray())
            }
        }
    }

    fun setLastestWeatherList(src: Array<Weather>) {
        var list = hashMapOf<String, Weather>()
        src.forEach {
            list.put(it.location?.name ?: context.getString(R.string.unknownLocation), it)
        }
        lastestWeatherLiveData.value = list
        saveLatestWeather(list)
    }


    private suspend fun getCurrentWeather(coroutineScope: CoroutineScope) {

        var response =
            currentLocationLiveData.value?.let {
                WeatherApiServis.getForecastWeatherApi(
                    it,
                    aqi = "yes"
                )
            }

        coroutineScope.launch(Dispatchers.Main) {
            response?.forecast?.forecastday?.let {
                it.forEach {
                    if (it.date != null) {
                        it.dayName = getDayByDate(it.date!!)
                    }
                }
            }
        }

        if (response != null) {
            coroutineScope.launch(Dispatchers.Main) {
                primaryWeatherLiveData.value = response!!
            }
        } else {
            Log.e("TAG", "Main Fragment View Modal start: response=null ")
        }

    }

    fun getDayByDate(date: String): String {
        var informat = SimpleDateFormat("yyyy-MM-dd")
        var date = informat.parse(date)
        var outFormat = SimpleDateFormat("EEEE")
        // println(outFormat.format(date))
        return outFormat.format(date)
    }

    private suspend fun getAllWeather(
        locationList: List<String>,
        coroutineScope: CoroutineScope
    ) {
        try {
            coroutineScope.launch(Dispatchers.Main) {
                allLocationWeathersLiveData.value = arrayListOf()
            }

            locationList.forEach {
                var response = WeatherApiServis.getCurrentWeatherApi(it)
                if (response != null) {

                    coroutineScope.launch(Dispatchers.Main) {
                        allLocationWeathersLiveData.value =
                            arrayListOf(
                                *allLocationWeathersLiveData.value!!.toTypedArray(),
                                response
                            )
                    }
                } else {
                    Log.e("TAG", "Main Fragment View Modal start: response=null ")
                }

            }
        } catch (_: Exception) {

        }


    }

    fun addLocation(location: String) {
        locationListLiveData.value =
            locationListLiveData.value?.let { arrayListOf(*it.toTypedArray(), location) }
        saveLocationsToDatabase()
    }

    fun swapLocation(firstIndex: Int, secondIndex: Int) {
        locationListLiveData.value?.let {
            var array = it.toTypedArray()
            var firstItem = array.get(firstIndex)
            array[firstIndex] = array[secondIndex]
            array[secondIndex] = firstItem
            locationListLiveData.value = arrayListOf(*array)
            saveLocationsToDatabase()
        }
    }

    fun deleteLocation(index: Int) {
        locationListLiveData.value?.let {
            it.removeAt(index)
            locationListLiveData.value = ArrayList(it)
            saveLocationsToDatabase(it)
        }
    }

    fun saveLocationsToDatabase(list: ArrayList<String>? = null) {
        val list = list ?: locationListLiveData.value

        viewModelScope.launch(Dispatchers.IO) {
            try {
                list?.let {
                    val dao = LocationDatabase(context).getDao()
                    dao.deleteAll()
                    var list = list.map {
                        return@map DatabaseLocation(name = it)
                    }
                    dao.insertAll(*list.toTypedArray())

                }
            } catch (e: Exception) {

            }
        }
    }

    fun getLocationFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dao = LocationDatabase(context).getDao()
                var list = dao.getAll().map {
                    return@map it.name
                }
                withContext(Dispatchers.Main) {
                    locationListLiveData.value = ArrayList(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message)

            }
        }
    }

    fun updateWeatherDetailList(weather: Weather) {
        var detailList = arrayListOf<WeatherDetail>()
        //Air Pollution Layout
        detailList.add(
            WeatherDetail(
                1,
                context.getDrawable(R.drawable.baseline_air_24)!!,
                context.getString(R.string.airPollution) + "(EPA)",
                "${weather.current?.airQuality?.us_epa_index} ${EPAIndexList.get(weather.current?.airQuality?.us_epa_index)}",
                (weather.current?.airQuality?.us_epa_index ?: 0).toDouble() * 100 / 6
            )
        )
        //Wind Layout
        detailList.add(
            WeatherDetail(
                3,
                context.getDrawable(R.drawable.baseline_air_24)!!,
                context.getString(R.string.wind),
                "${weather.current?.windKph} km/h",
                weather.current?.windDegree?.toDouble() ?: 0.0

            )
        )
        //humadity
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_water_drop_24)!!,
                context.getString(R.string.humadity),
                "${weather.current?.humidity ?: 0} %",
                weather.current?.humidity?.toDouble() ?: 0.0
            )
        )
        //pressure
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_water_drop_24)!!,
                context.getString(R.string.pressure),
                "${weather.current?.pressureMb ?: 0} millibars ",
                50.0,
                otherText = " ${weather.current?.pressureIn ?: 0} inches"
            )
        )
        //uv
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_wb_sunny_24)!!,
                context.getString(R.string.UVIndex),
                "UV Index ${weather.current?.uv ?: 0}",
                (Math.min(weather.current?.uv ?: 8, 8) * 100 / 8).toDouble()
            )
        )
        //Precipitation
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_water_drop_24)!!,
                context.getString(R.string.precipitationAmount),
                "${weather.current?.precipMm ?: 0} millimeters",
                50.0,
                otherText = "${weather.current?.precipIn ?: 0} inches"
            )
        )
        //cloud
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_cloud_24)!!,
                context.getString(R.string.cloudCoverAsPercentage),
                "${weather.current?.cloud ?: 0.0} %",
                weather.current?.cloud?.toDouble() ?: 0.0
            )
        )
//visibility
        detailList.add(
            WeatherDetail(
                2,
                context.getDrawable(R.drawable.baseline_visibility_24)!!,
                context.getString(R.string.averageVisibility),
                "${weather.current?.visKm ?: 0.0} Km",
                100 - (Math.min(weather.current?.visKm ?: 0.0, 8.0) * 100 / 8),
                otherText = "${weather.current?.visMiles ?: 0.0} Miles",
            )
        )










        weatherDetailListLiveData.value = detailList
    }

    private val locationLiveDataObserver = object : Observer<android.location.Location> {
        override fun onChanged(t: android.location.Location?) {
            val location = "${t?.latitude} , ${t?.longitude}"
            currentLocationLiveData.value = location
            if (!locationListLiveData.value.isNullOrEmpty()) {
                locationListLiveData.value!![0] = location
            } else {
                locationListLiveData.value?.add(0, location)
            }
        }

    }

    fun changeCurrentLocationByGps(viewLifecycleOwner: LifecycleOwner) {
        locationData.start()
        if (locationData.getLiveGpsLocation().hasActiveObservers()) {
            locationData.getLiveGpsLocation().observeForever(locationLiveDataObserver)
        } else {
            locationData.getLiveGpsLocation().observe(viewLifecycleOwner, locationLiveDataObserver)
        }

    }

    fun changeCurrentLocation(location: String) {
        locationData.getLiveGpsLocation().removeObserver(locationLiveDataObserver)
        currentLocationLiveData.value = location
    }

    fun searchLocation(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var response = WeatherApiServis.getSearchLocation(location)
                response?.let {
                    withContext(Dispatchers.Main) {
                        searchLocationListLiveData.value = arrayListOf(*it.toTypedArray())
                    }
                }


            } catch (e: Exception) {

            }
        }
    }



}