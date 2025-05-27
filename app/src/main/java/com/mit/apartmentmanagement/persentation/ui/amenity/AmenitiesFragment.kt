package com.mit.apartmentmanagement.persentation.ui.amenity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mit.apartmentmanagement.databinding.FragmentAmenitiesBinding
import com.mit.apartmentmanagement.domain.model.Amenity
import com.mit.apartmentmanagement.domain.model.AmenityType
import com.mit.apartmentmanagement.persentation.ui.ApartmentDetailActivity
import com.mit.apartmentmanagement.persentation.viewmodels.AmenitiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AmenitiesFragment : Fragment() {

    companion object {
        private const val TAG = "AmenitiesFragment"
    }

    private var _binding: FragmentAmenitiesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AmenitiesViewModel by viewModels()

    private lateinit var sportSection: AmenitySectionView
    private lateinit var entertainmentSection: AmenitySectionView
    private lateinit var relaxationSection: AmenitySectionView
    private lateinit var communitySection: AmenitySectionView
    private lateinit var otherSection: AmenitySectionView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAmenitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSections()
        setupObservers()
        setupSwipeRefresh()
    }

    private fun setupSections() {
        // Initialize section views
        sportSection = AmenitySectionView(
            sectionView = binding.sportSection.root,
            type = AmenityType.SPORT,
            onAmenityClicked = ::onAmenityClicked
        ).apply {
            setTitle(viewModel.getTypeDisplayName(AmenityType.SPORT))
            setDescription(viewModel.getTypeDescription(AmenityType.SPORT))
        }

        entertainmentSection = AmenitySectionView(
            sectionView = binding.entertainmentSection.root,
            type = AmenityType.ENTERTAINMENT,
            onAmenityClicked = ::onAmenityClicked
        ).apply {
            setTitle(viewModel.getTypeDisplayName(AmenityType.ENTERTAINMENT))
            setDescription(viewModel.getTypeDescription(AmenityType.ENTERTAINMENT))
        }

        relaxationSection = AmenitySectionView(
            sectionView = binding.relaxationSection.root,
            type = AmenityType.RELAXATION,
            onAmenityClicked = ::onAmenityClicked
        ).apply {
            setTitle(viewModel.getTypeDisplayName(AmenityType.RELAXATION))
            setDescription(viewModel.getTypeDescription(AmenityType.RELAXATION))
        }

        communitySection = AmenitySectionView(
            sectionView = binding.communitySection.root,
            type = AmenityType.COMMUNITY,
            onAmenityClicked = ::onAmenityClicked
        ).apply {
            setTitle(viewModel.getTypeDisplayName(AmenityType.COMMUNITY))
            setDescription(viewModel.getTypeDescription(AmenityType.COMMUNITY))
        }

        otherSection = AmenitySectionView(
            sectionView = binding.otherSection.root,
            type = AmenityType.OTHER,
            onAmenityClicked = ::onAmenityClicked
        ).apply {
            setTitle(viewModel.getTypeDisplayName(AmenityType.OTHER))
            setDescription(viewModel.getTypeDescription(AmenityType.OTHER))
        }
    }

    private fun setupObservers() {
        // Observe grouped amenities
        lifecycleScope.launch {
            viewModel.groupedAmenities.collectLatest { groupedAmenities ->
                Log.d(TAG, "Grouped amenities updated: ${groupedAmenities.keys}")
                updateSections(groupedAmenities)
            }
        }

        // Observe loading state
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // Observe error state
        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                if (error != null) {
                    Log.e(TAG, "Error: $error")
                    // TODO: Show error message to user
                }
            }
        }

        // Observe all amenities for empty state
        lifecycleScope.launch {
            viewModel.allAmenities.collectLatest { amenities ->
                binding.emptyStateLayout.visibility = 
                    if (amenities.isEmpty() && !viewModel.isLoading.value) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateSections(groupedAmenities: Map<AmenityType, List<Amenity>>) {
        // Update each section with its amenities
        sportSection.updateAmenities(groupedAmenities[AmenityType.SPORT] ?: emptyList())
        entertainmentSection.updateAmenities(groupedAmenities[AmenityType.ENTERTAINMENT] ?: emptyList())
        relaxationSection.updateAmenities(groupedAmenities[AmenityType.RELAXATION] ?: emptyList())
        communitySection.updateAmenities(groupedAmenities[AmenityType.COMMUNITY] ?: emptyList())
        otherSection.updateAmenities(groupedAmenities[AmenityType.OTHER] ?: emptyList())

        // Hide sections with no data (optional - you might want to show empty states instead)
        val hasAnyAmenities = groupedAmenities.values.any { it.isNotEmpty() }
        
        if (!hasAnyAmenities && !viewModel.isLoading.value) {
            // All sections are empty - the main empty state will be shown
            hideAllSections()
        } else {
            showRelevantSections(groupedAmenities)
        }
    }

    private fun hideAllSections() {
        sportSection.hide()
        entertainmentSection.hide()
        relaxationSection.hide()
        communitySection.hide()
        otherSection.hide()
    }

    private fun showRelevantSections(groupedAmenities: Map<AmenityType, List<Amenity>>) {
        // Show all sections - individual sections will handle their own empty states
        sportSection.show()
        entertainmentSection.show()
        relaxationSection.show()
        communitySection.show()
        otherSection.show()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshAmenities()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun onAmenityClicked(amenity: Amenity) {
        Log.d(TAG, "Amenity clicked: ${amenity.name}")
        // Navigate to amenity detail - for now using ApartmentDetailActivity
        val intent = Intent(requireContext(), ApartmentDetailActivity::class.java)
        intent.putExtra("amenity", amenity)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}