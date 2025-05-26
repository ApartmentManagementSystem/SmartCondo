# Invoice Chart Feature - ViewPager2 Implementation

## Tổng quan
Tính năng mới này hiển thị chart hóa đơn của từng căn hộ trong HomeFragment sử dụng ViewPager2, cho phép người dùng swipe qua các slide để xem chart của từng căn hộ riêng biệt.

## Các thành phần đã được implement

### 1. HomeViewModel Updates
- **Thêm GetInvoiceForChartUseCase**: Lấy dữ liệu hóa đơn từ API
- **Thêm LiveData mới**:
  - `chartInvoices`: Danh sách hóa đơn gốc từ API
  - `groupedInvoices`: Hóa đơn được group theo tên căn hộ
  - `apartmentNames`: Danh sách tên căn hộ đã sort
- **Function processChartInvoices()**: Xử lý group hóa đơn theo apartmentName
- **Function getInvoicesForApartment()**: Lấy hóa đơn của một căn hộ cụ thể

### 2. InvoiceChartPagerAdapter
- **RecyclerView.Adapter** cho ViewPager2
- **ChartViewHolder**: Hiển thị chart cho từng căn hộ
- **Features**:
  - Hiển thị tên căn hộ
  - Bar chart với MPAndroidChart
  - Xử lý trường hợp không có dữ liệu
  - Animation khi load chart
  - Touch interaction và drag

### 3. Layout Updates
- **fragment_home.xml**: 
  - Thay thế single chart bằng ViewPager2
  - Thêm page indicators
  - Thêm counter hiển thị "1/3" apartments
- **item_invoice_chart.xml**: Layout cho từng slide chart

### 4. HomeFragment Updates
- **Setup ViewPager2**: Với page transformer và callbacks
- **Page Indicators**: Dots hiển thị slide hiện tại
- **Auto-update**: Counter và indicators khi swipe
- **Error Handling**: Hiển thị message khi không có dữ liệu

## Cách sử dụng

### Trong HomeFragment:
1. ViewPager2 sẽ tự động load khi có dữ liệu từ API
2. User có thể swipe left/right để xem chart của các căn hộ khác
3. Page indicators (dots) hiển thị slide hiện tại
4. Counter "1/3" hiển thị vị trí hiện tại

### Data Flow:
```
API → GetInvoiceForChartUseCase → HomeViewModel.processChartInvoices() 
→ groupedInvoices & apartmentNames → InvoiceChartPagerAdapter → ViewPager2
```

## Testing

### TestInvoiceChartActivity
- Activity đơn giản để test tính năng
- Hiển thị ViewPager2 với charts
- Log dữ liệu để debug
- Error handling

### Cách test:
1. Run app và vào HomeFragment
2. Kiểm tra ViewPager2 hiển thị charts
3. Swipe để test navigation
4. Kiểm tra page indicators
5. Xem logs để debug data flow

## Code Quality Features

### Clean Architecture:
- **Domain Layer**: GetInvoiceForChartUseCase
- **Data Layer**: InvoiceRepository.getInvoiceForChart()
- **Presentation Layer**: HomeViewModel, HomeFragment, Adapter

### Error Handling:
- Loading states
- Empty data states
- Network error handling
- Graceful fallbacks

### Performance:
- ViewPager2 với offscreenPageLimit = 1
- Efficient data binding
- Smooth animations
- Memory-conscious implementation

### UI/UX:
- Material Design components
- Smooth page transitions
- Visual feedback (indicators)
- Responsive layout
- Accessibility support

## Các file đã thay đổi:

1. `HomeViewModel.kt` - Thêm logic xử lý chart data
2. `HomeFragment.kt` - Implement ViewPager2 và UI logic
3. `InvoiceChartPagerAdapter.kt` - Adapter mới cho ViewPager2
4. `fragment_home.xml` - Layout updates
5. `item_invoice_chart.xml` - Layout cho chart items
6. `TestInvoiceChartActivity.kt` - Test activity (optional)

## Dependencies:
- MPAndroidChart (đã có sẵn)
- ViewPager2 (AndroidX)
- Material Components
- Hilt (Dependency Injection)

## Future Enhancements:
- Thêm filter theo thời gian
- Export chart thành image
- Thêm chart types khác (Line, Pie)
- Zoom và pan functionality
- Comparison mode giữa các căn hộ 