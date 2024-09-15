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

        // Xử lý từ khóa tìm kiếm từ ViewModel
        searchViewModel.searchResults.observe(this) { locations ->
            Log.e("SearchActivity","Location 1:"+locations.get(0).address)
            searchAdapter.submitList(locations)
        }

        searchViewModel.searchKeyword.observe(this) { keyword ->
            searchAdapter = SearchAdapter(
                onItemClicked = { location ->
                    location.position?.lat?.let { location.position.lng?.let { it1 ->
                        openGoogleMaps(it,
                            it1
                        )
                    } }
                },
                searchKeyword = keyword
            )
            binding.recyclerView.adapter = searchAdapter
        }

        // Hiển thị hoặc ẩn trạng thái tải
        searchViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // Hiển thị lỗi nếu có
        searchViewModel.errorState.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Log.e("SearchActivity","error:"+errorMessage)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý tìm kiếm khi người dùng nhập từ khóa
        binding.searchEditText.addTextChangedListener { query ->
            searchViewModel.searchLocation(query.toString())
        }
    }

    // Mở Google Maps với vị trí đã chọn
    private fun openGoogleMaps(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("geo:$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}

