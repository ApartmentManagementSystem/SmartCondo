package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.databinding.ItemAmenityBinding
import com.mit.apartmentmanagement.domain.model.Amenity

class AmenityAdapter(private val onAmenityClicked: (Amenity) -> Unit) :
    ListAdapter<Amenity, AmenityAdapter.AmenityViewHolder>(AmenityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder {
        val binding = ItemAmenityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AmenityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AmenityViewHolder(
        private val binding: ItemAmenityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val amenity = currentList[position]
                    onAmenityClicked(amenity)
                }
            }
        }

        fun bind(amenity: Amenity) {
            binding.amenity = amenity
            binding.executePendingBindings()
        }
    }

    private class AmenityDiffCallback : DiffUtil.ItemCallback<Amenity>() {
        override fun areItemsTheSame(oldItem: Amenity, newItem: Amenity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Amenity, newItem: Amenity): Boolean {
            return oldItem == newItem
        }
    }
}