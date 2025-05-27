package com.mit.apartmentmanagement.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Amenity(
    val id: String,
    val name: String,
    val description: String,
    val type: AmenityType,
    val status: String, // active, maintenance, closed
    val openTime: String,
    val closeTime: String,
    val requiresBooking: Boolean,
    val maxCapacity: Int,
    val location: String,
    val usageFee: Double,
    val imageUrl: String?,
    val lastMaintenanceDate: String,
    val notes: String? = null,
    val unavailableSlots: List<String> = emptyList()
): Parcelable

