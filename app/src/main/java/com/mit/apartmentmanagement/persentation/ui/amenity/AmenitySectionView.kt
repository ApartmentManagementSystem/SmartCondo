package com.mit.apartmentmanagement.persentation.ui.amenity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.domain.model.Amenity
import com.mit.apartmentmanagement.domain.model.AmenityType
import com.mit.apartmentmanagement.persentation.ui.adapter.AmenityAdapter

class AmenitySectionView(
    private val sectionView: View,
    private val type: AmenityType,
    private val onAmenityClicked: (Amenity) -> Unit
) {
    
    private val titleTextView = sectionView.findViewById<TextView>(R.id.tvSectionTitle)
    private val descriptionTextView = sectionView.findViewById<TextView>(R.id.tvSectionDescription)
    private val recyclerView = sectionView.findViewById<RecyclerView>(R.id.rvAmenities)
    private val emptyLayout = sectionView.findViewById<View>(R.id.layoutSectionEmpty)
    
    private val adapter = AmenityAdapter(onAmenityClicked)
    
    init {
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@AmenitySectionView.adapter
        }
    }
    
    fun setTitle(title: String) {
        titleTextView.text = title
    }
    
    fun setDescription(description: String) {
        descriptionTextView.text = description
    }
    
    fun updateAmenities(amenities: List<Amenity>) {
        adapter.submitList(amenities)
        
        if (amenities.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }
    
    fun hide() {
        sectionView.visibility = View.GONE
    }
    
    fun show() {
        sectionView.visibility = View.VISIBLE
    }
} 