package com.mit.apartmentmanagement.persentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityMainBinding
import com.mit.apartmentmanagement.domain.util.NetworkResult
import com.mit.apartmentmanagement.persentation.ui.auth.ProfileUserActivity
import com.mit.apartmentmanagement.persentation.ui.login.LoginActivity
import com.mit.apartmentmanagement.persentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.btnMenu.setOnClickListener {
            binding.main.open()
        }
        setUptNavigationView()
        setupBottomNavigation()
        setupTitle()
        observerLogoutResult()
        setContentView(binding.root)
    }

    private fun observerLogoutResult() {
        mainViewModel.logoutResult.observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    //showLoading
                }
            }

        }
    }




private fun setupTitle() {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        binding.tvHeader.text = destination.label
    }
}

private fun setUptNavigationView() {
    binding.navigationView.setNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.my_profile -> {
                startActivity(Intent(this, ProfileUserActivity::class.java))
                true
            }

            R.id.notification -> {
                //navController.navigate(R.id.notificationFragment)
                binding.main.close()
                true
            }

            R.id.language -> {
                // navController.navigate(R.id.languageFragment)
                binding.main.close()
                true
            }

            R.id.contact -> {
                //navController.navigate(R.id.contactFragment)
                binding.main.close()
                true
            }

            R.id.logout -> {
                handleLogout()
                true
            }

            else -> false
        }
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

    private fun handleLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Log out") { _, _ ->
                mainViewModel.logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}