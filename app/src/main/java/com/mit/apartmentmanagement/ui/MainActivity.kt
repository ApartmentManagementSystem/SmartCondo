package com.mit.apartmentmanagement.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.mit.apartmentmanagement.R
import com.mit.apartmentmanagement.databinding.ActivityMainBinding
import com.mit.apartmentmanagement.ui.auth.LoginActivity
import com.mit.apartmentmanagement.ui.auth.ProfileUserActivity
import com.mit.apartmentmanagement.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.btnMenu.setOnClickListener {
            binding.main.open()
        }
        setUptNavigationView()
        setupBottomNavigation()
        setupTiltle()
        setContentView(binding.root)
        controlStatusBar()

        authViewModel.logoutResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                Log.d("LoginViewModel", "Đăng xuất thành công")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }.onFailure { error ->
                Toast.makeText(this, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }




    private fun setupTiltle() {
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
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
            .setPositiveButton("Đăng xuất") { _, _ ->
                lifecycleScope.launch {
                    authViewModel.logout()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()


    }
    private fun controlStatusBar() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_real)

        // Kiểm tra chế độ Dark Mode
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Nếu đang ở Dark Mode -> Chữ status bar là trắng
            window.decorView.systemUiVisibility = 0
        } else {
            // Nếu đang ở Light Mode -> Chữ status bar là đen
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}