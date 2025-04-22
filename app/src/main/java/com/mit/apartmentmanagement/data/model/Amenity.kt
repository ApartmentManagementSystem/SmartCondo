package com.mit.apartmentmanagement.data.model

data class Amenity(
    val amenityId: String,
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
) {
    enum class AmenityType {
        SPORT,
        ENTERTAINMENT,
        RELAXATION,
        COMMUNITY,
        OTHER
    }
}

