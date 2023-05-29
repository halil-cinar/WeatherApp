package com.halil.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.databinding.DaylyWeatherItemBinding
import com.halil.weatherapp.entity.Day
import com.halil.weatherapp.entity.Forecastday
import com.halil.weatherapp.entity.Weather

class DaylyWeatherAdapter(var list:ArrayList<Forecastday>):RecyclerView.Adapter<DaylyWeatherAdapter.ViewHolder>() {

    class ViewHolder(var binding:DaylyWeatherItemBinding):RecyclerView.ViewHolder(binding.root){
        fun changeWeather(weather: Forecastday){
            binding.currentItem=weather;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=DataBindingUtil.inflate<DaylyWeatherItemBinding>(
            inflater, R.layout.dayly_weather_item,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.changeWeather(list.get(position))
    }
    fun updateList(list: ArrayList<Forecastday>){
        this.list=list
        notifyDataSetChanged()
    }

}