package com.mit.apartmentmanagement.persentation.ui.request

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mit.apartmentmanagement.R

class AddRequestActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_request)
        
        // TODO: Implement add request functionality
        setupToolbar()
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            title = "Add New Request"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 