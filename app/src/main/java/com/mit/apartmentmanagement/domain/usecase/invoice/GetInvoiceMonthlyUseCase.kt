package com.mit.apartmentmanagement.domain.usecase.invoice

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvoiceMonthlyUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Flow<PagingData<InvoiceMonthly>> {
        return invoiceRepository.fetchInvoices(page, size)
    }
}