package com.mit.apartmentmanagement.persentation.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mit.apartmentmanagement.R

class AmenityBindingAdapter {
    companion object {
        @BindingAdapter("amenityImage")
        @JvmStatic
        fun setAmenityImage(view: ImageView, imageUrl: String?) {
            if (imageUrl.isNullOrBlank()) {
                view.setImageResource(R.drawable.ic_placeholder)
            } else {
                Glide.with(view.context)
                    .load(imageUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_error)
                            .centerCrop()
                    )
                    .into(view)
            }
        }
    }
}