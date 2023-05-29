package com.halil.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.databinding.ForecastWeatherRecyclerViewBinding
import com.halil.weatherapp.entity.CurrentItem
import com.halil.weatherapp.entity.Hour


public class CurrentWeatherItemAdapter(var list: List<Hour>):RecyclerView.Adapter<CurrentWeatherItemAdapter.ViewHolder>() {
    class ViewHolder(val binding:ForecastWeatherRecyclerViewBinding):RecyclerView.ViewHolder(binding.root){
       fun changeCurrentItem(currentItem: Hour){
           binding.item=currentItem
       }
    }

   lateinit var binding:ForecastWeatherRecyclerViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.forecast_weather_recycler_view,
            parent,
            false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.changeCurrentItem(list.get(position))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun updateList(list: List<Hour>){
        this.list=list
        notifyDataSetChanged()
    }
}