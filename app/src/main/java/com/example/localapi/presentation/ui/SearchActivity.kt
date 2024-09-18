package com.example.localapi.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localapi.Constant.LOCATION_PERMISSION_REQUEST_CODE
import com.example.localapi.databinding.ActivitySearchBinding
import com.example.localapi.presentation.adapter.SearchAdapter
import com.example.localapi.presentation.viewmodel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest = LocationRequest.create().apply {
        interval = 5000  // Thời gian giữa các lần cập nhật (5 giây)
        fastestInterval = 2000  // Thời gian ngắn nhất giữa các lần cập nhật (2 giây)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    val position = "circle:$lat,$lng;r=100000"
                    Log.e("SearchActivity", "Updated position: $position")
                    searchViewModel.updatePosition(position)
                }
            }
        }
        getLastKnownLocation()
        startLocationUpdates()
        //adapter
        setupRecycleView()
        //observe
        observeViewModel()

        binding.searchEditText.addTextChangedListener { query ->
            //searchViewModel.searchLocation(query.toString())  // Gửi từ khóa tìm kiếm
            searchViewModel.searchLocationOfArea(query.toString())
        }
    }


    private fun observeViewModel() {
        searchViewModel.searchResults.observe(this) { locations ->
            if (locations != null) {
                if (locations.isNotEmpty()) {
                    searchAdapter.submitList(locations)
                    Log.e("SearchActivity", "Location1:" + locations.get(0).title)
                } else {
                    searchAdapter.submitList(emptyList())
                }
            }
        }

        searchViewModel.searchKeyword.observe(this) { keyword ->
            searchAdapter.updateSearchKeyword(keyword)
        }

        searchViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        searchViewModel.errorState.observe(this) { errorMessage ->
            if (errorMessage != null) {
                when (errorMessage.toString()) {
                    "Network error, please try again later" -> Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupRecycleView() {
        searchAdapter = SearchAdapter(onItemClicked = { location ->
            if (location.latitude != null && location.longitude != null) {
                openGoogleMaps(location.latitude, location.longitude, location.addressName)
            } else {
                Toast.makeText(this, "Invalid location data", Toast.LENGTH_SHORT).show()
            }
        }, searchKeyword = "")

        binding.recyclerView.adapter = searchAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
                val position = "circle:$lat,$lng"
                Log.e("SearchActivity", "position: $position")
                searchViewModel.updatePosition(position)  // Gọi updatePosition để lưu vị trí vào ViewModel
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                // Bạn có thể xử lý thêm logic bật GPS hoặc kiểm tra dịch vụ vị trí tại đây
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
        }


    }


    private fun openGoogleMaps(lat: Double, lng: Double,title: String) {
        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($title)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        val activities = packageManager.queryIntentActivities(mapIntent, 0)
        if (activities.isNotEmpty()) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "No map applications available", Toast.LENGTH_SHORT).show()
        }

        // Kiểm tra xem Google Maps có được cài đặt không
        val googleMapsInstalled = mapIntent.resolveActivity(packageManager) != null
        if (googleMapsInstalled) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.google.android.apps.maps")
            )
            startActivity(intent)

        }

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Đăng ký lắng nghe cập nhật vị trí liên tục
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}



