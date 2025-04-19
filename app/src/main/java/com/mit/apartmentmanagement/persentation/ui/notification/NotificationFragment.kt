package com.mit.apartmentmanagement.persentation.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mit.apartmentmanagement.databinding.FragmentNotificationBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.NotificationViewModel
import com.mit.apartmentmanagement.persentation.util.NetworkResult
import kotlinx.coroutines.launch



class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentNotificationBinding.inflate(layoutInflater)
        fetchDataNotification()
        observeNotifications()
        setupRecyclerView()
        setupSearchView()
        return binding.root
    }

    private fun observeNotifications() {
        notificationViewModel.notifications.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        notificationAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),"Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    // Xử lý khi đang tải dữ liệu
                }
            }
        }
    }

    private fun fetchDataNotification() {
        lifecycleScope.launch{
            notificationViewModel.getNotifications()
        }
    }

    private fun setupRecyclerView() {

    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notificationAdapter.filterData(newText ?: "")
                return true
            }
        })
    }

}