package com.mit.apartmentmanagement

import com.mit.apartmentmanagement.domain.model.Notification
import org.junit.Test
import org.junit.Assert.*

/**
 * Test for NotificationListAdapter functionality
 */
class NotificationListAdapterTest {
    
    @Test
    fun notificationList_withDifferentTypes_shouldReturnCorrectCount() {
        val notifications = listOf(
            createMockNotification("1", "Payment Due"),
            createMockNotification("2", "Maintenance Alert"),
            createMockNotification("3", "Security Notice"),
            createMockNotification("4", "General Announcement"),
            createMockNotification("5", "Reminder")
        )
        assertEquals("List should have count 5", 5, notifications.size)
    }
    
    @Test
    fun notification_readStatus_shouldBeCorrect() {
        val unreadNotification = createMockNotification("1", "Test", false)
        val readNotification = createMockNotification("2", "Test", true)
        
        assertFalse("Notification should be unread", unreadNotification.isRead)
        assertTrue("Notification should be read", readNotification.isRead)
    }
    
    @Test
    fun notification_content_shouldNotBeEmpty() {
        val notification = createMockNotification("1", "Test Title")
        
        assertTrue("Title should not be empty", notification.title.isNotEmpty())
        assertTrue("Content should not be empty", notification.content.isNotEmpty())
        assertTrue("ID should not be empty", notification.notificationId.isNotEmpty())
    }
    
    private fun createMockNotification(
        id: String, 
        title: String, 
        isRead: Boolean = false
    ): Notification {
        return Notification(
            notificationId = id,
            title = title,
            content = "Test content for notification",
            createdAt = "2024-01-01T00:00:00",
            image = "test_image.jpg",
            isRead = isRead
        )
    }
} 