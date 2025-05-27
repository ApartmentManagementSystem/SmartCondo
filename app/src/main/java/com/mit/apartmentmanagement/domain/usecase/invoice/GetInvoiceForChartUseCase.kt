package com.mit.apartmentmanagement.domain.usecase.invoice

import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoiceForChartUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke() = invoiceRepository.getInvoiceForChart()
}