package com.zeiris.cherryweather.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zeiris.cherryweather.R
import com.zeiris.cherryweather.ui.adapter.SearchAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: SearchAdapter
    private lateinit var locationClient: FusedLocationProviderClient

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        adapter = SearchAdapter()
        cities.layoutManager = LinearLayoutManager(this)
        cities.adapter = adapter

        if (supportActionBar != null) {
            val actionBar: ActionBar? = supportActionBar
            actionBar?.title = null
        }

        viewModel.fetchWeatherByCityId(1851632)
        viewModel.fetchWeatherByCityId(709930)
        viewModel.weather
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                progress_bar.visibility = View.GONE
                adapter.updateWeatherList(it)
            }

        coordinate_search_button.setOnClickListener {
            checkLocation()
        }

        card_remove_button.setOnClickListener {
            viewModel.deleteWeather(adapter.checkedWeather).subscribe {
                adapter.clearCheckedList()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            this.maxWidth = Int.MAX_VALUE
            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.fetchWeatherByCityName(it) }
                    this@apply.setQuery("", false)
                    this@apply.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }


    private fun checkLocation() {
        val locationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            locationClient = LocationServices.getFusedLocationProviderClient(this)
            locationClient.lastLocation.addOnSuccessListener {
                progress_bar.visibility = View.VISIBLE
                viewModel.fetchWeatherByCoordinates(it.latitude, it.longitude)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
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
        private const val LOCATION_REQUEST_CODE = 1
    }
}
