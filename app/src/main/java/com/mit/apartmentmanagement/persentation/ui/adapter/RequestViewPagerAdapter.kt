package com.mit.apartmentmanagement.persentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.persentation.ui.request.RequestListFragment

class RequestViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val requestStatuses = listOf(
        RequestStatus.RECEIVED,
        RequestStatus.IN_PROGRESS,
        RequestStatus.RESOLVED
    )

    override fun getItemCount(): Int = requestStatuses.size

    override fun createFragment(position: Int): Fragment {
        return RequestListFragment.newInstance(requestStatuses[position])
    }

    fun getTabTitle(position: Int): String {
        return when (requestStatuses[position]) {
            RequestStatus.RECEIVED -> "Received"
            RequestStatus.IN_PROGRESS -> "In Progress"
            RequestStatus.RESOLVED -> "Resolved"
        }
    }
} 