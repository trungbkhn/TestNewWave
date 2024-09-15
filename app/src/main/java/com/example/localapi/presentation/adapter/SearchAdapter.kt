package com.example.localapi.presentation.adapter

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
    private val searchKeyword: String
) : ListAdapter<Location, SearchAdapter.SearchViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location, searchKeyword)
    }

    inner class SearchViewHolder(
        private val binding: ItemLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location, keyword: String) {
            binding.titleTextView.text = location.title?.let { highlightKeyword(it, keyword) }
            binding.addressTextView.text =
                location.address?.label?.let { highlightKeyword(it, keyword) }

            // Khi người dùng nhấn vào địa điểm, mở Google Maps
            binding.root.setOnClickListener {
                onItemClicked(location)
            }
        }

        private fun highlightKeyword(text: String, keyword: String): SpannableString {
            val spannable = SpannableString(text)
            val startIndex = text.indexOf(keyword, ignoreCase = true)
            if (startIndex >= 0) {
                val endIndex = startIndex + keyword.length
                spannable.setSpan(
                    ForegroundColorSpan(Color.YELLOW),  // Màu sắc nổi bật
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return spannable
        }
    }

    class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}


