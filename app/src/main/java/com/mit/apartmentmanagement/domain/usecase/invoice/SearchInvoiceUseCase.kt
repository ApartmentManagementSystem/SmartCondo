package com.mit.apartmentmanagement.domain.usecase.invoice

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchInvoiceUseCase @Inject constructor(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(
        search: String,
        page: Int,
        size: Int
    ): Flow<PagingData<InvoiceMonthly>> {
        return invoiceRepository.searchInvoicesByDate(search, page, size)
    }
}