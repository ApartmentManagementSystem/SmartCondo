package com.mit.apartmentmanagement.domain.model

import com.mit.apartmentmanagement.R

enum class ServiceType(val iconRes: Int, val title: String) {
    ELECTRICITY(R.drawable.ic_electricity, "Hóa đơn điện"),
    WATER(R.drawable.ic_water, "Hóa đơn nước"),
    PARKING(R.drawable.ic_parking, "Phí gửi xe"),
    COMMON(R.drawable.ic_common_service, "Dịch vụ chung")
} 