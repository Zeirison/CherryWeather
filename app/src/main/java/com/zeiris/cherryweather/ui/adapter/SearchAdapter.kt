package com.zeiris.cherryweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.databinding.ItemSearchBinding

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
                executePendingBindings()
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