package com.mit.apartmentmanagement.domain.usecase.invoice

import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvoiceDetailUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(invoiceId: String): Flow<Result<InvoiceMonthly>> {
        return invoiceRepository.getInvoiceDetail(invoiceId)
    }
} 