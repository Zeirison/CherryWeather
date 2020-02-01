package com.zeiris.cherryweather.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.zeiris.cherryweather.databinding.FragmentSearchBinding
import com.zeiris.cherryweather.ui.adapter.SearchAdapter
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private val adapter = SearchAdapter()

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        context ?: return binding.cities.rootView

        binding.cities.adapter = adapter

        viewModel.getWeatherByCityId(1851632).subscribeOn(Schedulers.io())
        viewModel.getWeatherByCityId(709930).subscribeOn(Schedulers.io())

        viewModel.weather.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.cities.rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

}