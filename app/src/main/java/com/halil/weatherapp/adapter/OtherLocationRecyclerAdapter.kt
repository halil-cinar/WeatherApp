package com.halil.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.databinding.OtherLocationRecyclerViewBinding
import com.halil.weatherapp.entity.Weather
import com.halil.weatherapp.listener.WeatherOnClickListener

class OtherLocationRecyclerAdapter(var list: ArrayList<Weather>,var clickListener: WeatherOnClickListener):RecyclerView.Adapter<OtherLocationRecyclerAdapter.ViewHolder>() {

    class ViewHolder(var binding: OtherLocationRecyclerViewBinding,var context: Context):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater=LayoutInflater.from(parent.context)
        var binding=DataBindingUtil.inflate<OtherLocationRecyclerViewBinding>(
            layoutInflater, R.layout.other_location_recycler_view,parent,false
        )
        return ViewHolder(binding,parent.context)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.weather=list.get(position)
        holder.binding.clickListener=clickListener

    }
    fun updateList(list: ArrayList<Weather>){
        this.list=list
        notifyDataSetChanged()
    }

    fun onRowMoved(fromPosition: Int, toPosition: Int) {
         notifyItemMoved(fromPosition, toPosition)
    }

}