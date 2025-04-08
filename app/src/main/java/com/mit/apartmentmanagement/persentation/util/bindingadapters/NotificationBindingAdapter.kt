package com.mit.apartmentmanagement.persentation.util.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


class NotificationBindingAdapter {
    companion object {

        @JvmStatic
        @BindingAdapter("setText")
        fun setText(textView: TextView, content: String) {
            textView.text=content
        }

        @JvmStatic
        @BindingAdapter("setVisibility")
        fun setVisibility(imageView: ImageView, isVisible:Boolean){
            if(isVisible){
                imageView.visibility=View.VISIBLE
            }else{
                imageView.visibility= View.GONE
            }
        }
    }

}