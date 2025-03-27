package com.mit.apartmentmanagement.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.btnMenu.setOnClickListener {
            binding.main.open()
        }

        setupBottomNavigation()
        setupTiltle()
        setContentView(binding.root)

    }

    private fun setupTiltle() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvHeader.text = destination.label
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.amenitiesFragment -> {
                    navController.navigate(R.id.amenitiesFragment)
                    true
                }

                R.id.invoiceFragment -> {
                    navController.navigate(R.id.invoiceFragment)
                    true
                }

                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.notificationFragment -> {
                    navController.navigate(R.id.notificationFragment)
                    true
                }

                R.id.requestFragment -> {
                    navController.navigate(R.id.requestFragment)
                    true
                }

                else -> false
            }

        }
    }
}