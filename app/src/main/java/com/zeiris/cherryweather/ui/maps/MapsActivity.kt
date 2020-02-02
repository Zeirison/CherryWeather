package com.zeiris.cherryweather.ui.maps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zeiris.cherryweather.R
import kotlinx.android.parcel.Parcelize


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var options: MarkerOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        options = intent.getParcelableExtra(EXTRA_MAP_OPTIONS)

        val mapFragment: SupportMapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap
            ?.addMarker(MarkerOptions().title(options.title).position(options.latLng))
            ?.showInfoWindow()
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(options.latLng, 10F))
    }

    companion object {
        fun startActivity(context: Context, markerOptions: MarkerOptions) {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(EXTRA_MAP_OPTIONS, markerOptions)
            context.startActivity(intent)
        }

        private const val EXTRA_MAP_OPTIONS = "EXTRA_MAP_OPTIONS"
    }

    @Parcelize
    data class MarkerOptions(var title: String, var latLng: LatLng) : Parcelable

}