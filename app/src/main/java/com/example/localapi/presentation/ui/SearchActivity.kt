package com.example.localapi.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localapi.databinding.ActivitySearchBinding
import com.example.localapi.presentation.adapter.SearchAdapter
import com.example.localapi.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo adapter một lần
        searchAdapter = SearchAdapter(onItemClicked = { location ->
            openGoogleMaps(location.latitude, location.longitude)
        }, searchKeyword = "")

        // Set adapter cho RecyclerView
        binding.recyclerView.adapter = searchAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        searchViewModel.searchResults.observe(this) { locations ->
            if (locations != null) {
                if (locations.isNotEmpty()) {
                    searchAdapter.submitList(locations)  // Cập nhật danh sách trong adapter
                } else {
                    searchAdapter.submitList(emptyList())  // Đảm bảo adapter nhận danh sách rỗng
                }
            } // Cập nhật danh sách trong adapter
            //Log.e("SearchActivity", "Location1 : " + locations[0])
        }


        searchViewModel.searchKeyword.observe(this) { keyword ->
            searchAdapter.updateSearchKeyword(keyword)
        }


        searchViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // Quan sát trạng thái lỗi
        searchViewModel.errorState.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Log.e("SearchActivity", "error: $errorMessage")
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý tìm kiếm khi nhập
        binding.searchEditText.addTextChangedListener { query ->
            searchViewModel.searchLocation(query.toString())  // Gửi từ khóa tìm kiếm
        }
    }

    private fun openGoogleMaps(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("geo:$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}



