package com.mit.apartmentmanagement.domain.usecase.invoice

import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetSixInvoiceMonthlyUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke() = invoiceRepository.getSixInvoiceMonthly()
}