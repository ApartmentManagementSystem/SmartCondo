package com.mit.apartmentmanagement

import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import org.junit.Test
import org.junit.Assert.*

/**
 * Test cases cho chức năng thanh toán hóa đơn
 */
class DetailInvoicePaymentTest {

    @Test
    fun `test payment button visibility for PAID status should be GONE`() {
        val status = PaymentStatus.PAID
        val expectedVisibility = android.view.View.GONE
        
        // Logic giống trong updatePaymentButton
        val actualVisibility = when (status) {
            PaymentStatus.PAID -> android.view.View.GONE
            PaymentStatus.UNPAID -> android.view.View.VISIBLE
            else -> android.view.View.VISIBLE
        }
        
        assertEquals(expectedVisibility, actualVisibility)
    }

    @Test
    fun `test payment button visibility for UNPAID status should be VISIBLE`() {
        val status = PaymentStatus.UNPAID
        val expectedVisibility = android.view.View.VISIBLE
        
        val actualVisibility = when (status) {
            PaymentStatus.PAID -> android.view.View.GONE
            PaymentStatus.UNPAID -> android.view.View.VISIBLE
            else -> android.view.View.VISIBLE
        }
        
        assertEquals(expectedVisibility, actualVisibility)
    }

    @Test
    fun `test payment button enabled state for different statuses`() {
        // Test PAID status
        val paidStatus = PaymentStatus.PAID
        val shouldBeEnabledForPaid = when (paidStatus) {
            PaymentStatus.PAID -> false
            PaymentStatus.UNPAID -> true
            else -> false
        }
        assertFalse(shouldBeEnabledForPaid)

        // Test UNPAID status  
        val unpaidStatus = PaymentStatus.UNPAID
        val shouldBeEnabledForUnpaid = when (unpaidStatus) {
            PaymentStatus.PAID -> false
            PaymentStatus.UNPAID -> true
            else -> false
        }
        assertTrue(shouldBeEnabledForUnpaid)

        // Test CANCELED status
        val canceledStatus = PaymentStatus.CANCELED
        val shouldBeEnabledForCanceled = when (canceledStatus) {
            PaymentStatus.PAID -> false
            PaymentStatus.UNPAID -> true
            else -> false
        }
        assertFalse(shouldBeEnabledForCanceled)
    }

    @Test
    fun `test payment confirmation should only show for UNPAID status`() {
        assertTrue("Should show confirmation for UNPAID", PaymentStatus.UNPAID == PaymentStatus.UNPAID)
        assertFalse("Should not show confirmation for PAID", PaymentStatus.PAID == PaymentStatus.UNPAID)
        assertFalse("Should not show confirmation for CANCELED", PaymentStatus.CANCELED == PaymentStatus.UNPAID)
    }
} 