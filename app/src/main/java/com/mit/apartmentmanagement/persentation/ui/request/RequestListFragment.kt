package com.mit.apartmentmanagement.persentation.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mit.apartmentmanagement.databinding.FragmentRequestListBinding
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.persentation.ui.adapter.RequestListAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.RequestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestListFragment : Fragment() {
    
    private var _binding: FragmentRequestListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var requestAdapter: RequestListAdapter
    private var requestStatus: RequestStatus? = null
    
    private val viewModel: RequestViewModel by viewModels()
    
    companion object {
        private const val ARG_REQUEST_STATUS = "request_status"
        
        fun newInstance(status: RequestStatus): RequestListFragment {
            return RequestListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REQUEST_STATUS, status)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestStatus = arguments?.getSerializable(ARG_REQUEST_STATUS) as? RequestStatus
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        loadRequestsByStatus()
    }
    
    private fun setupRecyclerView() {
        requestAdapter = RequestListAdapter { request ->
            // Handle request item click
            onRequestClicked(request)
        }
        
        binding.recyclerViewRequests.apply {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    
    private fun observeViewModel() {
        // Observe requests data
        lifecycleScope.launch {
            viewModel.requests.collectLatest { pagingData ->
                requestAdapter.submitData(pagingData)
            }
        }
        
        // Observe loading state
        lifecycleScope.launch {
            requestAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.refresh is LoadState.Loading
                val isEmpty = loadStates.refresh is LoadState.NotLoading && requestAdapter.itemCount == 0
                
                // Show/hide empty state
                binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.recyclerViewRequests.visibility = if (isEmpty) View.GONE else View.VISIBLE
                
                // Handle errors
                val errorState = loadStates.source.append as? LoadState.Error
                    ?: loadStates.source.prepend as? LoadState.Error
                    ?: loadStates.append as? LoadState.Error
                    ?: loadStates.prepend as? LoadState.Error
                    ?: loadStates.refresh as? LoadState.Error
                
                errorState?.let {
                    // TODO: Show error message
                }
            }
        }
    }
    
    private fun loadRequestsByStatus() {
        requestStatus?.let { status ->
            viewModel.loadRequestsByStatus(status)
        } ?: run {
            viewModel.loadAllRequests()
        }
    }

    
    private fun onRequestClicked(request: Request) {
        // TODO: Navigate to request detail screen
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 