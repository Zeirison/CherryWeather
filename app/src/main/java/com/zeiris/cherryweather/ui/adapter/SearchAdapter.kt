package com.zeiris.cherryweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.google.android.gms.maps.model.LatLng
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.databinding.ItemSearchBinding
import com.zeiris.cherryweather.ui.maps.MapsActivity


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val listDiffer by lazy {
        AsyncListDiffer<Weather>(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder<Weather>(DIFF_CALLBACK).build()
        )
    }

    fun updateWeatherList(list: List<Weather>) {
        listDiffer.submitList(list)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val weather = getItem(position)
        weather.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Weather) {
            binding.apply {
                weather = item
                forecast = item.getNearestForecast()

                weatherCard.setOnClickListener {
                    if (weatherCard.isChecked) {
                        weatherCard.isChecked = false
                    } else {
                        val temperature = item.getNearestForecast().main.getTempInCelsius()
                        val latLng = LatLng(
                            item.coordinates.lat,
                            item.coordinates.lon
                        )
                        val options = MapsActivity.MarkerOptions(
                            "${item.name} $temperature℃", latLng
                        )
                        MapsActivity.startActivity(weatherCard.context, options)
                    }
                }

                weatherCard.setOnLongClickListener {
                    weatherCard.isChecked = !weatherCard.isChecked
                    true
                }
            }
        }

    }

    override fun getItemCount(): Int = listDiffer.currentList.count()

    private fun getItem(position: Int): Weather = listDiffer.currentList[position]

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Weather>() {
            override fun areItemsTheSame(
                oldItem: Weather,
                newItem: Weather
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Weather,
                newItem: Weather
            ) = oldItem == newItem
        }
    }

}