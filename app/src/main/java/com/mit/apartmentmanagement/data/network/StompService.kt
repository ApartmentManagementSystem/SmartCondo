package com.mit.apartmentmanagement.data.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.mit.apartmentmanagement.data.model.notification.Notification
import com.mit.apartmentmanagement.data.network.interceptors.RefreshTokenInterceptor
import com.mit.apartmentmanagement.domain.model.MonthlyInvoice
import com.mit.apartmentmanagement.domain.model.Request
import com.mit.apartmentmanagement.util.Constant.URL_STOMP
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StompService @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenInterceptor: RefreshTokenInterceptor,
) {

    private val serverUrl = URL_STOMP
    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl)
    private var compositeDisposable = CompositeDisposable() // Dùng cho lifecycle, nếu cần
    private val gson = Gson()
    private var isConnected: Boolean = false

    /**
     * Kết nối đến STOMP server và lắng nghe lifecycle events.
     */
    private fun connect() {
            val token = tokenManager.getAccessToken()
            val headers = listOf(
                StompHeader("Authorization", "Bearer $token"))

            stompClient.connect(headers)
            compositeDisposable.add(
                stompClient.lifecycle()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ lifecycleEvent ->
                        when (lifecycleEvent.type) {
                            LifecycleEvent.Type.OPENED -> {
                                Log.d("STOMP", "Kết nối mở thành công")
                                isConnected = true
                            }
                            LifecycleEvent.Type.ERROR -> {
                                // Kiểm tra nếu lỗi do token hết hạn (ví dụ: Unauthorized)
                                if (lifecycleEvent.exception?.message?.contains(
                                        "Unauthorized",
                                        true
                                    ) == true
                                ) {
                                    handleTokenExpiration()
                                }

                                Log.e("STOMP", "Lỗi kết nối", lifecycleEvent.exception)
                            }
                            LifecycleEvent.Type.CLOSED ->
                                Log.d("STOMP", "Kết nối đã đóng")
                            LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                                Log.e("STOMP", "Heartbeat từ server thất bại")
                            else ->
                                Log.w("STOMP", "Sự kiện không xác định: ${lifecycleEvent.type}")
                        }
                    }, { throwable ->
                        Log.e("STOMP", "Lỗi lifecycle: ${throwable.message}")
                    })
            )
        }

    private fun handleTokenExpiration() {
        disconnect()
        //refreshTokenInterceptor.refreshToken()
        connect()
    }

    /**
     * Ngắt kết nối và giải phóng tài nguyên.
     */
    fun disconnect() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        stompClient.disconnect()
        isConnected = false
        compositeDisposable = CompositeDisposable()
    }

    /**
     * Tái kết nối sau khi mất kết nối (nếu cần).
     */
    private fun reconnect() {
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("STOMP", "Đang thử kết nối lại...")
            connect()
        }, 5000) // Thử kết nối lại sau 5 giây
    }

    /**
     * Trả về Flow cập nhật thông báo.
     */
    fun observeNotificationUpdates(): Flow<Notification> = callbackFlow {
        if (!isConnected) connect()

        val disposable = stompClient.topic("topic/notification/email")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message: StompMessage ->
                Log.d("STOMP", "Nhận thông báo: ${message.payload}")
                val notification = gson.fromJson(message.payload, Notification::class.java)
                trySend(notification)
            }, { error ->
                Log.e("STOMP", "Lỗi nhận thông báo: ${error.message}", error)
            })

        awaitClose { disposable.dispose() }
    }



    /**
     * Trả về Flow cập nhật invoice.
     */
    fun observeInvoiceUpdates(): Flow<MonthlyInvoice> = callbackFlow {
        if (!isConnected) connect()

        val disposable = stompClient.topic("/topic/invoice")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message: StompMessage ->
                Log.d("STOMP", "Nhận invoice: ${message.payload}")
                val invoice = gson.fromJson(message.payload, MonthlyInvoice::class.java)
                trySend(invoice)
            }, { error ->
                Log.e("STOMP", "Lỗi nhận invoice: ${error.message}", error)
            })

        awaitClose { disposable.dispose() }
    }

    /**
     * Trả về Flow cập nhật trạng thái yêu cầu.
     */
    fun observeRequestUpdates(): Flow<Request> = callbackFlow {
        if (!isConnected) connect()

        val disposable = stompClient.topic("/topic/request")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message: StompMessage ->
                Log.d("STOMP", "Nhận request: ${message.payload}")
                val request = gson.fromJson(message.payload, Request::class.java)
                trySend(request)
            }, { error ->
                Log.e("STOMP", "Lỗi nhận request: ${error.message}", error)
            })

        awaitClose { disposable.dispose() }
    }
}
