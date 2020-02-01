package com.zeiris.cherryweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeiris.cherryweather.data.model.Weather
import com.zeiris.cherryweather.databinding.ItemSearchBinding

class SearchAdapter : PagedListAdapter<Weather, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weather = getItem(position)
        weather?.let { (holder as SearchViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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