package com.zeiris.cherryweather.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.card.MaterialCardView
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.databinding.ItemSearchBinding
import com.zeiris.cherryweather.ui.maps.MapsActivity
import com.zeiris.cherryweather.ui.search.OnListSizeChangedListener


class SearchAdapter(private val listener: OnListSizeChangedListener) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val listDiffer by lazy {
        AsyncListDiffer<Weather>(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder<Weather>(DIFF_CALLBACK).build()
        )
    }

    val checkedWeather: ArrayList<Weather> = arrayListOf()

    fun updateWeatherList(list: List<Weather>) {
        listDiffer.submitList(list)
    }

    fun clearCheckedList() {
        checkedWeather.clear()
        listSizeChanged()
    }

    private fun listSizeChanged() {
        listener.onListChanged()
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

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Weather) {
            binding.apply {
                weather = item
                forecast = item.getNearestForecast()
            }

            val card = binding.weatherCard as MaterialCardView
            card.isChecked = false
            card.setOnClickListener {
                if (!card.isChecked && checkedWeather.size == 0) {
                    openGoogleMaps(card.context, item)
                }
                card.isChecked = changeCardState(card.isChecked, false, item)
            }

            card.setOnLongClickListener {
                card.isChecked = changeCardState(card.isChecked, true, item)
                true
            }
        }

    }

    private fun changeCardState(isChecked: Boolean, isLong: Boolean, item: Weather): Boolean {
        if (checkedWeather.size == 0 && !isLong) return false

        if (!isChecked) {
            checkedWeather.add(item)
            listSizeChanged()
            return true
        }

        if (!isChecked && checkedWeather.size > 0) {
            checkedWeather.add(item)
            listSizeChanged()
            return true
        }

        checkedWeather.remove(item)
        listSizeChanged()
        return false
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

        fun openGoogleMaps(context: Context, weather: Weather) {
            val temperature = weather.getNearestForecast().main.getTempInCelsius()
            val latLng = LatLng(
                weather.coordinates.lat,
                weather.coordinates.lon
            )
            val options = MapsActivity.MarkerOptions(
                "${weather.name} $temperature℃", latLng
            )
            MapsActivity.startActivity(context, options)
        }
    }

}