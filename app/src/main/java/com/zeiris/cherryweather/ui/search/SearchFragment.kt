package com.zeiris.cherryweather.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zeiris.cherryweather.databinding.FragmentSearchBinding
import com.zeiris.cherryweather.ui.adapter.SearchAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private val adapter = SearchAdapter()

    private lateinit var locationClient: FusedLocationProviderClient

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        context ?: return binding.cities.rootView
        binding.cities.adapter = adapter

        viewModel.fetchWeatherByCityId(1851632)
        viewModel.fetchWeatherByCityId(709930)
        viewModel.weather
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.updateWeatherList(it)
            }

        return binding.cities.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coordinate_search_button.setOnClickListener {
            checkLocation()
        }
    }

    private fun checkLocation() {
        val locationPermission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            locationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
            locationClient.lastLocation.addOnSuccessListener {
                viewModel.fetchWeatherByCoordinates(it.latitude, it.longitude)
            }
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE &&
            grantResults.contains(PackageManager.PERMISSION_GRANTED)
        ) {
            checkLocation()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()

        private const val LOCATION_REQUEST_CODE = 1
    }

}