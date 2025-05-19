package com.mit.apartmentmanagement.persentation.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mit.apartmentmanagement.domain.model.Notification
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<PagingData<Notification>>(PagingData.empty())
    val notifications: StateFlow<PagingData<Notification>> = _notifications

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentQuery: String? = null

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                notificationRepository.getNotifications()
                    .cachedIn(viewModelScope)
                    .catch { e ->
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _notifications.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun searchNotifications(query: String) {
        if (query == currentQuery) return
        currentQuery = query
        viewModelScope.launch {
            _isLoading.value = true
            try {
                notificationRepository.searchNotifications(query)
                    .cachedIn(viewModelScope)
                    .catch { e ->
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _notifications.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun markNotificationAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                notificationRepository.markNotificationAsRead(notificationId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            try {
                notificationRepository.deleteNotification(notificationId)
                // Refresh the list after deletion
                loadNotifications()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
} 