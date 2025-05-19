package com.mit.apartmentmanagement.persentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mit.apartmentmanagement.databinding.ItemApartmentBinding
import com.mit.apartmentmanagement.domain.model.Apartment

class ApartmentAdapter (
    private val onApartmentClicked: (Apartment) -> Unit
) : ListAdapter<Apartment, ApartmentAdapter.ApartmentViewHolder>(
    ApartmentDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        val binding = ItemApartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApartmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApartmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

   inner class ApartmentViewHolder(private val binding: ItemApartmentBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val apartment = currentList[position]
                    onApartmentClicked(apartment)
                }
            }
        }
        fun bind(apartment: Apartment) {
            binding.apply {
                apartmentName.text = apartment.name
                apartmentAddress.text = apartment.buildingName
                Glide.with(apartmentImage)
                    .load(apartment.imageUrl)
                    .centerCrop()
                    .into(apartmentImage)
            }
        }
    }

    private class ApartmentDiffCallback : DiffUtil.ItemCallback<Apartment>() {
        override fun areItemsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem == newItem
        }
    }
} 