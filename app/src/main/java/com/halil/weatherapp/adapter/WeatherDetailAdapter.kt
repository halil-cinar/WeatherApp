package com.halil.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.databinding.WeatherDetailRecyclerItemBinding
import com.halil.weatherapp.entity.WeatherDetail
import com.halil.weatherapp.listener.WeatherDetailOnClickListener
import com.halil.weatherapp.listener.WeatherOnClickListener

class WeatherDetailAdapter(var list:ArrayList<WeatherDetail>):RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder>() {

    class ViewHolder(var binding: WeatherDetailRecyclerItemBinding):RecyclerView.ViewHolder(binding.root){
        fun changeDetail(weatherDetail: WeatherDetail){
            binding.weatherDetail=weatherDetail
        }
var visibility=false
        init {
            binding.clickListener=object :WeatherDetailOnClickListener{
                override fun onClick(view: View, weatherDetail: WeatherDetail) {
                    visibility=!visibility
                    binding.moreDetailVisibility=visibility
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater=LayoutInflater.from(parent.context)
        val binding=DataBindingUtil.inflate<WeatherDetailRecyclerItemBinding>(
            inflater, R.layout.weather_detail_recycler_item,parent,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.changeDetail(list.get(position))
    }

    fun updateList(list: ArrayList<WeatherDetail>){
        this.list=list
        notifyDataSetChanged()
    }

}