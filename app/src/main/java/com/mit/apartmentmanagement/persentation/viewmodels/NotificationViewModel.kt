package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mit.apartmentmanagement.data.model.Notification
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import com.mit.apartmentmanagement.persentation.util.networkconnect.NetworkListener
import com.mit.apartmentmanagement.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @OptIn(ExperimentalCoroutinesApi::class)
@Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkListener: NetworkListener,
): ViewModel(){

    private var _notifications: MutableLiveData<NetworkResult<List<Notification>>> = MutableLiveData()
    val notifications: MutableLiveData<NetworkResult<List<Notification>>> = _notifications

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getNotifications() {
        _notifications.value = NetworkResult.Loading()
        if (networkListener.isConnected()) {
            val response = notificationRepository.getNotificationsFromApi()
            if (response.isSuccessful) {
                response.body()?.let {
                    _notifications.value = NetworkResult.Success(it)
                    observeNotificationUpdates()
                }
            } else {
                _notifications.value = NetworkResult.Error(response.message())
            }

        }else{
            _notifications.value = NetworkResult.Error("No internet connection")
        }
    }



    suspend fun observeNotificationUpdates() {
        // Sử dụng collect để lắng nghe và xử lý thông báo mới từ repository
        notificationRepository.observeNotificationMessages().collect { newNotification ->
            // Kiểm tra xem _notifications.value có giá trị hay không và cộng thêm thông báo mới
            val currentNotifications = _notifications.value?.data.orEmpty()
            val updatedNotifications = currentNotifications + newNotification
            // Cập nhật lại _notifications với danh sách mới
            _notifications.value = NetworkResult.Success(updatedNotifications)
        }
    }


}