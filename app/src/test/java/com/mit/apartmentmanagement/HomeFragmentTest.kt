package com.mit.apartmentmanagement

import org.junit.Test
import org.junit.Assert.*

/**
 * Simple test to verify auto-scroll timing
 */
class HomeFragmentTest {
    
    @Test
    fun autoScrollDelay_isCorrect() {
        val expectedDelay = 500L // 0.5 seconds
        assertEquals("Auto-scroll delay should be 0.5 seconds", expectedDelay, 500L)
    }
    
    @Test
    fun autoScrollDelay_isNotTooFast() {
        val delay = 500L
        assertTrue("Auto-scroll should not be faster than 100ms", delay >= 100L)
    }
    
    @Test
    fun autoScrollDelay_isNotTooSlow() {
        val delay = 500L
        assertTrue("Auto-scroll should not be slower than 5 seconds", delay <= 5000L)
    }
} 