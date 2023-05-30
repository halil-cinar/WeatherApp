package com.halil.weatherapp.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.halil.weatherapp.R
import com.halil.weatherapp.view.MainActivity
import com.squareup.picasso.Picasso

fun ImageView.showImage(
    imgUrl: String,
    placeholder: CircularProgressDrawable = createDrawable(context)
) {
    var imgUrl = "http:" + imgUrl
    Picasso.get().load(imgUrl).placeholder(placeholder).into(this)

}

fun createDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

fun toCamelCase(expression: String): String {
    val words = expression.toLowerCase().split(" ")
    val camelCaseExpression = StringBuilder(words[0])

    for (i in 1 until words.size) {
        camelCaseExpression.append(words[i].capitalize())
    }

    return camelCaseExpression.toString()
}

@BindingAdapter("app:translateCondition")
fun translateCondition(view: TextView, condition: String?) {

    if (condition!=null) {
        view.text= getWeatherConditionTextList().get(toCamelCase(condition))
            ?.let { view.context.getString(it) } ?: condition
    }

}

private fun getWeatherConditionTextList() = hashMapOf<String, Int>(
    "day" to R.string.day,
    "sunny" to R.string.sunny,
    "partlyCloudy" to R.string.partlyCloudy,
    "cloudy" to R.string.cloudy,
    "overcast" to R.string.overcast,
    "mist" to R.string.mist,
    "patchyRainPossible" to R.string.patchyRainPossible,
    "patchySnowPossible" to R.string.patchySnowPossible,
    "patchySleetPossible" to R.string.patchySleetPossible,
    "patchyFreezingDrizzlePossible" to R.string.patchyFreezingDrizzlePossible,
    "thunderyOutbreaksPossible" to R.string.thunderyOutbreaksPossible,
    "blowingSnow" to R.string.blowingSnow,
    "blizzard" to R.string.blizzard,
    "fog" to R.string.fog,
    "freezingFog" to R.string.freezingFog,
    "patchyLightDrizzle" to R.string.patchyLightDrizzle,
    "lightDrizzle" to R.string.lightDrizzle,
    "freezingDrizzle" to R.string.freezingDrizzle,
    "heavyFreezingDrizzle" to R.string.heavyFreezingDrizzle,
    "patchyLightRain" to R.string.patchyLightRain,
    "lightRain" to R.string.lightRain,
    "moderateRainAtTimes" to R.string.moderateRainAtTimes,
    "moderateRain" to R.string.moderateRain,
    "heavyRainAtTimes" to R.string.heavyRainAtTimes,
    "heavyRain" to R.string.heavyRain,
    "lightFreezingRain" to R.string.lightFreezingRain,
    "moderateOrHeavyFreezingRain" to R.string.moderateOrHeavyFreezingRain,
    "lightSleet" to R.string.lightSleet,
    "moderateOrHeavySleet" to R.string.moderateOrHeavySleet,
    "patchyLightSnow" to R.string.patchyLightSnow,
    "lightSnow" to R.string.lightSnow,
    "patchyModerateSnow" to R.string.patchyModerateSnow,
    "moderateSnow" to R.string.moderateSnow,
    "patchyHeavySnow" to R.string.patchyHeavySnow,
    "heavySnow" to R.string.heavySnow,
    "icePellets" to R.string.icePellets,
    "lightRainShower" to R.string.lightRainShower,
    "moderateOrHeavyRainShower" to R.string.moderateOrHeavyRainShower,
    "torrentialRainShower" to R.string.torrentialRainShower,
    "lightSleetShowers" to R.string.lightSleetShowers,
    "moderateOrHeavySleetShowers" to R.string.moderateOrHeavySleetShowers,
    "lightSnowShowers" to R.string.lightSnowShowers,
    "moderateOrHeavySnowShowers" to R.string.moderateOrHeavySnowShowers,
    "lightShowersOfIcePellets" to R.string.lightShowersOfIcePellets,
    "moderateOrHeavyShowersOfIcePellets" to R.string.moderateOrHeavyShowersOfIcePellets,
    "patchyLightRainWithThunder" to R.string.patchyLightRainWithThunder,
    "moderateOrHeavyRainWithThunder" to R.string.moderateOrHeavyRainWithThunder,
    "patchyLightSnowWithThunder" to R.string.patchyLightSnowWithThunder,
    "moderateOrHeavySnowWithThunder" to R.string.moderateOrHeavySnowWithThunder,
    "night" to R.string.night,
    "clear" to R.string.clear,
)

@BindingAdapter("app:showImageByUrl")
fun showImageByUrl(view: ImageView, url: String?) {
    url?.let {
        view.showImage(it)
    }
}

@BindingAdapter("app:getTimeByDateTime")
fun getTimeByDateTime(view: TextView, dateTime: String?) {
    var dateTime = dateTime?.split(" ")
    var time = ""
    dateTime?.let {
        if (it.size > 1) {
            time = dateTime[1]
        }
    }
    view.text = time

}

@BindingAdapter("app:changeVisibility")
fun changeVisibility(view: View, visible: Boolean?) {
    visible?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("app:setSelected")
fun setSelected(view: TextView, isSelected: Boolean) {
    view.isSelected = isSelected
}

