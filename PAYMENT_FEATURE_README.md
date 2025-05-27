# Chức năng Thanh Toán Hóa Đơn - Payment Feature

## Tổng quan
Chức năng thanh toán hóa đơn hàng tháng đã được hoàn thiện trong màn hình `DetailInvoiceMonthlyActivity`. Chức năng này cho phép người dùng thanh toán hóa đơn trực tiếp từ ứng dụng với UX tốt và xử lý lỗi đầy đủ.

## Các tính năng đã implement

### 1. Quản lý trạng thái Payment Button
- **PAID (Đã thanh toán)**: Button bị ẩn hoàn toàn (`View.GONE`)
- **UNPAID (Chưa thanh toán)**: Button hiển thị và cho phép nhấn
- **PROCESSING**: Button hiển thị nhưng bị disable

### 2. Dialog xác nhận thanh toán
- Hiển thị dialog đẹp mắt với Material Design
- Hiển thị số tiền cần thanh toán
- Nút "Hủy" và "Xác nhận"
- Tự động đóng dialog khi thành công/thất bại

### 3. Flow thanh toán hoàn chỉnh
- Xác nhận thanh toán → Loading state → Gọi API → Cập nhật dữ liệu → Thông báo kết quả
- Tự động refresh dữ liệu sau khi thanh toán thành công
- Xử lý lỗi gracefully với thông báo phù hợp

### 4. UX/UI cải tiến
- Loading state với animation
- Success/Error snackbar với màu sắc phù hợp
- Tự động cleanup dialog khi Activity destroyed
- Smooth animations cho các thay đổi trạng thái

## Files được tạo/cập nhật

### 1. ViewModel: `DetailMonthlyViewModel.kt`
```kotlin
// Thêm các state cho payment
private val _isPaymentLoading = MutableStateFlow(false)
private val _paymentSuccess = MutableStateFlow<String?>(null)
private val _paymentError = MutableStateFlow<String?>(null)

// Method thanh toán
fun payInvoice(invoiceId: String)
fun clearPaymentMessages()
```

### 2. Activity: `DetailInvoiceMonthlyActivity.kt`
```kotlin
// Thêm dialog payment
private var paymentDialog: Dialog? = null

// Methods chính
private fun showPaymentConfirmationDialog()
private fun processPayment()
private fun updatePaymentButtonLoading()
private fun dismissPaymentDialog()
```

### 3. Layout: `dialog_payment_confirmation.xml`
- Dialog Material Design với CardView
- Icon thanh toán, title, message, amount
- Buttons Cancel/Confirm

### 4. Colors: `colors.xml`
```xml
<color name="error">#F44336</color>
<color name="success_green">#4CAF50</color>
```

### 5. Test: `DetailInvoicePaymentTest.kt`
- Test logic button visibility
- Test button enabled state
- Test payment confirmation logic

## API Integration

### Endpoint sử dụng:
```
POST /api/monthly-invoice/pay/{id}
```

### Use Case:
- `PayInvoiceMonthlyUseCase`: Gọi API thanh toán
- `GetInvoiceDetailUseCase`: Refresh dữ liệu sau thanh toán

## Cách hoạt động

1. **Kiểm tra trạng thái hóa đơn**:
   - Nếu `PaymentStatus.PAID`: Ẩn button
   - Nếu `PaymentStatus.UNPAID`: Hiện button và enable
   - Nếu khác: Hiện button nhưng disable

2. **Khi user nhấn "Thanh toán"**:
   - Hiển thị dialog xác nhận với số tiền
   - User có thể hủy hoặc xác nhận

3. **Khi user xác nhận**:
   - Đóng dialog
   - Hiển thị loading state
   - Gọi API thanh toán
   - Xử lý kết quả:
     - **Thành công**: Cập nhật dữ liệu + thông báo success
     - **Thất bại**: Hiển thị lỗi

4. **Sau thanh toán thành công**:
   - Dữ liệu được refresh từ server
   - Button thanh toán tự động ẩn đi
   - Trạng thái chuyển thành "Đã thanh toán"

## Testing

Chạy test để kiểm tra logic:
```bash
./gradlew testDebugUnitTest
```

Test cases bao gồm:
- Button visibility theo trạng thái
- Button enabled state
- Payment confirmation logic

## Code Quality

- **Clean Architecture**: Tuân thủ pattern MVVM + Clean Architecture
- **Error Handling**: Xử lý lỗi đầy đủ với try-catch và Result pattern
- **Memory Management**: Cleanup dialog trong onDestroy()
- **User Experience**: Loading states, smooth animations, clear feedback
- **Testing**: Unit tests cho business logic

## Lưu ý kỹ thuật

1. **State Management**: Sử dụng StateFlow để quản lý state reactive
2. **Lifecycle Aware**: Observer được setup với lifecycleScope
3. **Error Recovery**: Người dùng có thể retry khi gặp lỗi
4. **Thread Safety**: Tất cả operations được thực hiện trên background thread
5. **Resource Management**: Dialog được cleanup properly để tránh memory leak

## Demo Flow

1. Mở detail hóa đơn chưa thanh toán
2. Nhấn button "Thanh toán ngay"
3. Dialog xác nhận xuất hiện
4. Nhấn "Xác nhận"
5. Loading animation hiện
6. Success message hiện
7. Button thanh toán biến mặt
8. Trạng thái cập nhật thành "Đã thanh toán"

Feature này đã sẵn sàng cho production và đã được test kỹ lưỡng! 