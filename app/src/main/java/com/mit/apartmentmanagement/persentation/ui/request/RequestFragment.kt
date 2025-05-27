package com.mit.apartmentmanagement.persentation.ui.request

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.mit.apartmentmanagement.databinding.FragmentRequestBinding
import com.mit.apartmentmanagement.persentation.ui.adapter.RequestViewPagerAdapter
import com.mit.apartmentmanagement.persentation.viewmodels.RequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: RequestViewPagerAdapter
    private val viewModel: RequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupFloatingActionButton()
    }

    private fun setupViewPager() {
        viewPagerAdapter = RequestViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getTabTitle(position)
        }.attach()
    }

    private fun setupFloatingActionButton() {
        binding.fabAddRequest.setOnClickListener {
            // TODO: Navigate to AddRequestActivity
            // For now, we'll just show a placeholder
            navigateToAddRequest()
        }
    }

    private fun navigateToAddRequest() {
        val intent = Intent(context, AddRequestActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}