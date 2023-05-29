package com.halil.weatherapp.view

import android.graphics. BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.halil.weatherapp.R
import com.halil.weatherapp.adapter.CurrentWeatherItemAdapter
import com.halil.weatherapp.adapter.DaylyWeatherAdapter
import com.halil.weatherapp.adapter.OtherLocationRecyclerAdapter
import com.halil.weatherapp.adapter.SearchLocationRecyclerAdapter
import com.halil.weatherapp.adapter.WeatherDetailAdapter
import com.halil.weatherapp.databinding.FragmentMainBinding
import com.halil.weatherapp.entity.SearchLocation
import com.halil.weatherapp.entity.Weather
import com.halil.weatherapp.listener.SearchLocationOnClickListener
import com.halil.weatherapp.listener.WeatherOnClickListener
import com.halil.weatherapp.viewModel.MainFragmentViewModel
import kotlinx.android.synthetic.main.current_slide_bar.hourlyWeatherRecyclerView
import kotlinx.android.synthetic.main.fragment_main.allLocationRecycler
import kotlinx.android.synthetic.main.fragment_main.mainFragmentSwipeLayout
import kotlinx.android.synthetic.main.fragment_main.searchLocation
import kotlinx.android.synthetic.main.fragment_main.sliding_pane_layout
import java.lang.Exception
import java.time.temporal.TemporalField
import java.util.Calendar


class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var viewModel: MainFragmentViewModel
    lateinit var hourlyWeatherAdapter: CurrentWeatherItemAdapter
    lateinit var daylyWeatherAdapter: DaylyWeatherAdapter
    lateinit var weatherDetailAdapter: WeatherDetailAdapter
    lateinit var otherLocationRecyclerAdapter: OtherLocationRecyclerAdapter
    lateinit var searchLocationRecyclerAdapter: SearchLocationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)

        viewModel.start(requireContext(), requireActivity(), viewLifecycleOwner)
        hourlyWeatherAdapter = CurrentWeatherItemAdapter(arrayListOf())
        binding.recyclerViewAdapter = hourlyWeatherAdapter
        daylyWeatherAdapter = DaylyWeatherAdapter(arrayListOf())
        binding.slideBarForecastRecyclerAdapter = daylyWeatherAdapter


        weatherDetailAdapter = WeatherDetailAdapter(arrayListOf())

        binding.slideBarWeatherDetailsAdapter = weatherDetailAdapter

        otherLocationRecyclerAdapter =
            OtherLocationRecyclerAdapter(arrayListOf(), weatherOnClickListener)
        binding.otherLocationRecyclerAdapter = otherLocationRecyclerAdapter

        searchLocationRecyclerAdapter= SearchLocationRecyclerAdapter(arrayListOf(),searchLocationOnClickListener)
        binding.searchLocationRecyclerAdapter=searchLocationRecyclerAdapter

        viewModel.changeCurrentLocationByGps(viewLifecycleOwner)

        searchLocation.setOnQueryTextListener(onQuaryTextListener)
        observeLiveData()



    }

    fun observeLiveData() {
        viewModel.primaryWeatherLiveData.observe(viewLifecycleOwner, Observer {
            binding.currentWeather = it.current
            binding.currentWeatherLocation = it.location
            binding.currentDay=it.forecast?.forecastday?.get(0)?.day
            it.forecast?.forecastday?.get(0)?.hour?.let {
                hourlyWeatherAdapter.updateList(it)
                val calendar=Calendar.getInstance()
                val currentHour=calendar.get(Calendar.HOUR_OF_DAY)
                val temp=it.find {
                    it.time?.split(" ")?.get(1)?.split(":")?.get(0)?.toInt() ==(currentHour)
                }

                hourlyWeatherRecyclerView.scrollToPosition(it.indexOf(temp))
            }
            it?.forecast?.forecastday?.let {
                daylyWeatherAdapter.updateList(it)
            }
            println(it.current?.airQuality?.us_epa_index)
            viewModel.updateWeatherDetailList(it)
            viewModel.setLastestWeatherList(arrayOf(it))
            //viewModel.allLocationWeathersLiveData.value?.set(0,it)


        })
        viewModel.allLocationWeathersLiveData.observe(viewLifecycleOwner, Observer {
            otherLocationRecyclerAdapter.updateList(it)

        })

        viewModel.isUpdating.observe(viewLifecycleOwner, Observer {
            mainFragmentSwipeLayout.isRefreshing = it

        })

        mainFragmentSwipeLayout.setOnRefreshListener {
            viewModel.update()
        }

        viewModel.weatherDetailListLiveData.observe(viewLifecycleOwner, Observer {
            weatherDetailAdapter.updateList(it)
        })

        viewModel.currentLocationLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.update();
        })

        viewModel.locationListLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.allWeatherUpdate()
        })

        viewModel.searchLocationListLiveData.observe(viewLifecycleOwner, Observer {
            searchLocationRecyclerAdapter.updateList(it)
        })

        var itemTouchHelper=ItemTouchHelper(locationRecyclerItemHelper)
        itemTouchHelper.attachToRecyclerView(allLocationRecycler)

    }

    var weatherOnClickListener = object : WeatherOnClickListener {
        override fun onClick(view: View, weather: Weather) {
            println(weather.location?.name)
            try {
                if(weather.location?.name==
                    viewModel.allLocationWeathersLiveData.value?.get(0)?.location?.name){
                    viewModel.changeCurrentLocationByGps(viewLifecycleOwner)
                }else{
                    weather.location?.let {
                        viewModel.changeCurrentLocation("${it.lat} , ${it.lon}")
                    }

                }

                sliding_pane_layout.closePane()

            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        override fun onLongClick(view: View, weather: Weather): Boolean {
            return true
        }

    }




    var locationRecyclerItemHelper=object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT){
        var paint=Paint()
        var isLeftSwiped=false
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            //mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            otherLocationRecyclerAdapter.onRowMoved(viewHolder.adapterPosition,target.adapterPosition)
            isLeftSwiped=false
            //viewModel.swapLocation(viewHolder.adapterPosition,target.adapterPosition)
            return true;
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position=viewHolder.adapterPosition
            when(direction){
                ItemTouchHelper.LEFT->{
                    if(position>0) {
                        viewModel.deleteLocation(position)
                        isLeftSwiped = true
                    }else{
                        Toast.makeText(requireContext(), "Geçerli Konumunuzu silemezsiniz", Toast.LENGTH_SHORT).show()
                        otherLocationRecyclerAdapter.notifyItemChanged(0)
                    }

                }
            }

        }


        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            paint.color=Color.alpha(0)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }


        override fun onChildDraw(
            c: Canvas,
            rv: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            //c.drawColor(Color.RED)

            paint.color=Color.RED
            @RequiresApi(Build.VERSION_CODES.Q)
            paint.blendMode=BlendMode.DARKEN

            if(isCurrentlyActive&&actionState == ItemTouchHelper.ACTION_STATE_SWIPE&&dX<0){
                val child = rv.getChildAt(viewHolder.adapterPosition)
                c.drawRect(
                    child?.left?.toFloat()?:0f,
                    child?.bottom?.toFloat()?:0f, child?.right?.toFloat()?:0f,
                    (child?.top)?.toFloat()?:0f, paint
                )

            }

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            // Öğenin ne kadar sola çekilmesi gerektiğini belirleyen metot
            return 0.85f
        }
    }

    var searchLocationOnClickListener=object : SearchLocationOnClickListener{
        override fun add(view: View, _searchLocation: SearchLocation?) {
            _searchLocation?.let {
                it.url?.let {
                    viewModel.addLocation(it)
                    viewModel.searchLocationListLiveData.value= arrayListOf()
                    viewModel.changeCurrentLocation(it)
                }
                searchLocation.setQuery("",false)

            }
        }

        override fun onClick(view: View, searchLocation: SearchLocation?) {

        }

    }
    var onQuaryTextListener=object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                viewModel.searchLocation(newText)
            }
            return true
        }

    }



}