package com.example.localapi.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.localapi.databinding.ItemLocationBinding
import com.example.localapi.domain.model.Location

class SearchAdapter(
    private val onItemClicked: (Location) -> Unit,
    private var searchKeyword: String  // Cập nhật var để có thể thay đổi
) : ListAdapter<Location, SearchAdapter.SearchViewHolder>(LocationDiffCallback()) {

    // Tạo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    // Bind dữ liệu vào ViewHolder
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location, searchKeyword)
    }

    // Thêm phương thức để cập nhật từ khóa tìm kiếm mà không cần tạo lại adapter
    fun updateSearchKeyword(newKeyword: String) {
        searchKeyword = newKeyword
        notifyDataSetChanged()  // Thông báo cập nhật lại toàn bộ danh sách
    }

    // ViewHolder hiển thị thông tin từng địa điểm
    inner class SearchViewHolder(
        private val binding: ItemLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location, keyword: String) {
            // Làm nổi bật từ khóa trong tiêu đề và địa chỉ
            binding.titleTextView.text = highlightKeyword(location.title, keyword) ?: ""
            binding.addressTextView.text =
                highlightKeyword(location.addressName, keyword) ?: ""

            // Khi người dùng nhấn vào địa điểm, thực hiện hành động mở Google Maps
            binding.root.setOnClickListener {
                onItemClicked(location)
            }
        }

        // Phương thức làm nổi bật từ khóa trong văn bản
        private fun highlightKeyword(text: String, keyword: String): SpannableString {
            val spannable = SpannableString(text)

            // Kiểm tra nếu từ khóa không rỗng và tồn tại trong văn bản
            if (keyword.isNotBlank()) {
                val startIndex = text.indexOf(keyword, ignoreCase = true)  // Tìm kiếm không phân biệt hoa thường
                if (startIndex >= 0) {  // Nếu tìm thấy từ khóa
                    val endIndex = startIndex + keyword.length
                    spannable.setSpan(
                        ForegroundColorSpan(Color.YELLOW),  // Màu vàng để làm nổi bật từ khóa
                        startIndex,
                        endIndex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            return spannable
        }
    }

    // DiffUtil để so sánh các mục trong danh sách
    class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}



