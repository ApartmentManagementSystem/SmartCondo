package com.mit.apartmentmanagement.domain.model

import java.time.LocalDateTime

data class MonthlyInvoice(
    val monthlyInvoiceId:Int,
    val apartmentId:Int,
    val paidAt: LocalDateTime,
    val status:Boolean,
    )
