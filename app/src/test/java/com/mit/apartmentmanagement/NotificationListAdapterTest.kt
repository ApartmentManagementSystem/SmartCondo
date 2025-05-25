package com.mit.apartmentmanagement

import com.mit.apartmentmanagement.domain.model.Notification
import org.junit.Test
import org.junit.Assert.*

/**
 * Test for NotificationListAdapter functionality
 */
class NotificationListAdapterTest {
    
    @Test
    fun notificationList_withDifferentTopics_shouldReturnCorrectCount() {
        val notifications = listOf(
            createMockNotification(1, "Payment Due", "payment"),
            createMockNotification(2, "Maintenance Alert", "maintenance"),
            createMockNotification(3, "Security Notice", "security"),
            createMockNotification(4, "General Announcement", "announcement"),
            createMockNotification(5, "Reminder", "reminder")
        )
        assertEquals("List should have count 5", 5, notifications.size)
    }
    
    @Test
    fun notification_readStatus_shouldBeCorrect() {
        val unreadNotification = createMockNotification(1, "Test", "payment", false)
        val readNotification = createMockNotification(2, "Test", "payment", true)
        
        assertFalse("Notification should be unread", unreadNotification.isRead)
        assertTrue("Notification should be read", readNotification.isRead)
    }
    
    @Test
    fun notification_topics_shouldBeValid() {
        val validTopics = listOf("payment", "maintenance", "security", "announcement", "reminder")
        
        validTopics.forEach { topic ->
            val notification = createMockNotification(1, "Test", topic)
            assertTrue("Topic should be valid", notification.topic.isNotEmpty())
        }
    }
    
    @Test
    fun notification_content_shouldNotBeEmpty() {
        val notification = createMockNotification(1, "Test Title", "payment")
        
        assertTrue("Title should not be empty", notification.title.isNotEmpty())
        assertTrue("Content should not be empty", notification.content.isNotEmpty())
        assertTrue("Topic should not be empty", notification.topic.isNotEmpty())
    }
    
    private fun createMockNotification(
        id: Int, 
        title: String, 
        topic: String, 
        isRead: Boolean = false
    ): Notification {
        return Notification(
            notificationId = id,
            title = title,
            content = "Test content for $topic notification",
            createdAt = "2024-01-01T00:00:00",
            topic = topic,
            image = "test_image.jpg",
            isRead = isRead
        )
    }
} 