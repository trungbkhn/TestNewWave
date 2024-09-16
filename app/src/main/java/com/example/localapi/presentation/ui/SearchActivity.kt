package com.example.localapi.presentation.ui

import android.annotation.SuppressLint
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
import com.example.localapi.domain.model.Location
import com.example.localapi.presentation.adapter.SearchAdapter
import com.example.localapi.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
//class SearchActivity : AppCompatActivity() {
//
//    private val searchViewModel: SearchViewModel by viewModels()
//
//    private lateinit var searchAdapter: SearchAdapter
//    private lateinit var binding: ActivitySearchBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        searchViewModel.searchResults.observe(this) { locations ->
//            Log.e("SearchActivity","Location 1:"+locations.get(0).address)
//            searchAdapter.submitList(locations)
//        }
//
//        searchViewModel.searchKeyword.observe(this) { keyword ->
//            searchAdapter = SearchAdapter(
//                onItemClicked = { location ->
//                    location.position?.lat?.let { location.position.lng?.let { it1 ->
//                        openGoogleMaps(it,
//                            it1
//                        )
//                    } }
//                },
//                searchKeyword = keyword
//            )
//            Log.e("SearchActivity","Location 1:")
//            binding.recyclerView.adapter = searchAdapter
//        }
//
//        searchViewModel.loading.observe(this) { isLoading ->
//            binding.progressBar.isVisible = isLoading
//        }
//
//        searchViewModel.errorState.observe(this) { errorMessage ->
//            if (errorMessage != null) {
//                Log.e("SearchActivity","error:"+errorMessage)
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.searchEditText.addTextChangedListener { query ->
//            searchViewModel.searchLocation(query.toString())
//        }
//    }
//
//    private fun openGoogleMaps(lat: Double, lng: Double) {
//        val gmmIntentUri = Uri.parse("geo:$lat,$lng")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
//    }
//}

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
            location.position?.lat?.let { lat ->
                location.position.lng?.let { lng ->
                    openGoogleMaps(lat, lng)
                }
            }
        }, searchKeyword = "")

        // Set adapter cho RecyclerView
        binding.recyclerView.adapter = searchAdapter

        // Quan sát kết quả tìm kiếm
        searchViewModel.searchResults.observe(this) { locations ->
            searchAdapter.submitList(locations)  // Cập nhật danh sách trong adapter
            Log.e("SearchActivity", "Location1 : " + locations.get(0))
        }

        // Quan sát từ khóa tìm kiếm (nếu cần để làm nổi bật từ khóa trong danh sách)
        searchViewModel.searchKeyword.observe(this) { keyword ->
            searchAdapter.updateSearchKeyword(keyword)  // Cập nhật từ khóa trong adapter
        }

        // Quan sát trạng thái loading
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



