package com.halil.weatherapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData

class LocationData(var context: Context,var activity: Activity) {

    companion object{
        public val GPS_REQUEST_CODE=1523

    }

private lateinit var locationListener: LocationListener
private lateinit var locationManager: LocationManager
private var locationLiveData=MutableLiveData<Location>()
fun start():Boolean{
    if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
) {
    Toast.makeText(context, "Konum izni vermeniz gerekiyor", Toast.LENGTH_LONG).show()
    requestPermission()
    return false
}
    locationListener=object:LocationListener{
        override fun onLocationChanged(p0: Location) {
            locationLiveData.value=p0
        }

    }
    locationManager= context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1*60*1000,1000f,locationListener)
return true
}

    fun requestPermission(){
        activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),GPS_REQUEST_CODE)

    }

    fun getGpsLocation():Location?{
        return locationLiveData.value
    }

    fun getLiveGpsLocation():MutableLiveData<Location>{
        return locationLiveData
    }

    fun getLastKnownLocation():Location?{
return if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
)
locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?:
locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
else null
    }


}