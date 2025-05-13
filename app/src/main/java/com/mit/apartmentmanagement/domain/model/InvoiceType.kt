package com.mit.apartmentmanagement.domain.model

import com.mit.apartmentmanagement.R

enum class InvoiceType(val iconRes: Int, val title: String) {
    ELECTRICITY(R.drawable.ic_electricity_invoice, "Electricity"),
    WATER(R.drawable.ic_water_invoice, "Water"),
    PARKING(R.drawable.ic_parking_invoice, "Parking"),
    SERVICE(R.drawable.ic_service_invoice, "Service")
} 