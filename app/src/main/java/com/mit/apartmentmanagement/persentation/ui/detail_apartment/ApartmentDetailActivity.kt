package com.mit.apartmentmanagement.persentation.ui.detail_apartment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityApartmentDetailBinding
import com.mit.apartmentmanagement.domain.model.Apartment
import com.mit.apartmentmanagement.persentation.viewmodels.DetailApartmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApartmentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApartmentDetailBinding
    private val viewModel : DetailApartmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Setup data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apartment_detail)
        binding.lifecycleOwner = this
        
        setupUI()
        handleIntent()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup back button
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleIntent() {
        val apartment = intent.getParcelableExtra<Apartment>("apartment")
        apartment?.let {
            viewModel.setApartment(it)
        } ?: run {
            Toast.makeText(this, "Lỗi: Không thể lấy thông tin căn hộ", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.apartment.collect { apartment ->
                apartment?.let {
                    binding.apartment = it
                    loadApartmentImage(it.imageUrl)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(this@ApartmentDetailActivity, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun loadApartmentImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .transform(RoundedCorners(16))
                .placeholder(R.drawable.img)
                .error(R.drawable.img)
                .into(binding.imageView)
        } else {
            // Use default image
            binding.imageView.setImageResource(R.drawable.img)
        }
    }
}