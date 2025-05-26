package com.mit.apartmentmanagement.persentation.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mit.apartmentmanagement.databinding.FragmentNotificationBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationListAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationLoadStateAdapter
import com.mit.apartmentmanagement.persentation.util.showToast
import com.mit.apartmentmanagement.persentation.viewmodels.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notificationListAdapter: NotificationListAdapter
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater)
        setupRecyclerView()
        setupSearchView()
        setupSwipeRefresh()
        observeNotifications()
        return binding.root
    }

    private fun setupRecyclerView() {
        notificationListAdapter = NotificationListAdapter(
            onNotificationClicked = { notification ->
                val intent = Intent(requireContext(), NotificationDetailActivity::class.java)
                intent.putExtra("notification", notification)
                startActivity(intent)
                notificationViewModel.markNotificationAsRead(notification.notificationId)
            },
            onMarkAsReadClicked = { notification ->
                    notificationViewModel.markNotificationAsRead(notification.notificationId)
            },
            onViewDetailsClicked = { notification ->
                val intent = Intent(requireContext(), NotificationDetailActivity::class.java)
                intent.putExtra("notification", notification)
                startActivity(intent)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationListAdapter.withLoadStateFooter(
                footer = NotificationLoadStateAdapter {
                    notificationListAdapter.retry()
                }
            )
            
            // Add item decoration for better spacing
            addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(
                requireContext(),
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(resources.getDrawable(android.R.color.transparent, null))
            })
        }

        notificationListAdapter.addLoadStateListener { loadState ->
            binding.apply {

                emptyState.isVisible = loadState.source.refresh is LoadState.NotLoading &&
                        notificationListAdapter.itemCount == 0
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
            }

            // Show error if any
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                requireContext().showToast(it.error.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { notificationViewModel.searchNotifications(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { notificationViewModel.searchNotifications(it) }
                return true
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            notificationListAdapter.refresh()
        }
    }
    private fun observeNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            notificationViewModel.notifications.collectLatest { pagingData ->
                notificationListAdapter.submitData(pagingData)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            notificationViewModel.error.collectLatest { error ->
                error?.let {
                    requireContext().showToast(it)
                    binding.emptyState.isVisible=true
                }
            }
        }
    }
}