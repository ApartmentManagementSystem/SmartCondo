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
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mit.apartmentmanagement.databinding.FragmentNotificationBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationAdapter
import com.mit.apartmentmanagement.persentation.ui.adapter.NotificationLoadStateAdapter
import com.mit.apartmentmanagement.persentation.ui.util.SwipeToDeleteCallback
import com.mit.apartmentmanagement.persentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater)
        setupRecyclerView()
        setupSearchView()
        setupSwipeRefresh()
        setupSwipeToDelete()
        observeNotifications()
        return binding.root
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(
            onNotificationClicked = { notification ->
                val intent = Intent(requireContext(), NotificationDetailActivity::class.java)
                intent.putExtra("notification", notification)
                startActivity(intent)
                notificationViewModel.markNotificationAsRead(notification.notificationId)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter.withLoadStateFooter(
                footer = NotificationLoadStateAdapter {
                    notificationAdapter.retry()
                }
            )
        }

        notificationAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                emptyState.isVisible = loadState.source.refresh is LoadState.NotLoading &&
                        notificationAdapter.itemCount == 0
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
            notificationAdapter.refresh()
        }
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val notification = notificationAdapter.snapshot()[position]
                
                notification?.let {
                    // Show undo snackbar
                    Snackbar.make(
                        binding.root,
                        "Notification deleted",
                        Snackbar.LENGTH_LONG
                    ).setAction("UNDO") {
                        // TODO: Implement undo delete functionality
                        notificationAdapter.notifyItemInserted(position)
                    }.show()

                    notificationViewModel.deleteNotification(it.notificationId)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun observeNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            notificationViewModel.notifications.collectLatest { pagingData ->
                notificationAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            notificationViewModel.error.collectLatest { error ->
                error?.let {
                    requireContext().showToast(it)
                }
            }
        }
    }
}