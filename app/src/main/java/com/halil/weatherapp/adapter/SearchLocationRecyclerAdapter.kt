package com.halil.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.databinding.SearchLocationOptionRecyclerItemBinding
import com.halil.weatherapp.entity.SearchLocation
import com.halil.weatherapp.listener.SearchLocationOnClickListener
import com.halil.weatherapp.listener.WeatherOnClickListener

class SearchLocationRecyclerAdapter(var list: ArrayList<SearchLocation>,var onClickListener: SearchLocationOnClickListener):RecyclerView.Adapter<SearchLocationRecyclerAdapter.ViewHolder>() {

    class ViewHolder(var binding:SearchLocationOptionRecyclerItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding=DataBindingUtil.inflate<SearchLocationOptionRecyclerItemBinding>(
            layoutInflater, R.layout.search_location_option_recycler_item,parent,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binding.searchLocation=list.get(position)
        holder.binding.clickListener=onClickListener
    }

    fun updateList(list: ArrayList<SearchLocation>){
        this.list=list
        notifyDataSetChanged()
    }

}