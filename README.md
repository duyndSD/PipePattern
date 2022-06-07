# PipePattern
Demo Pipe Pattern
Có vấn đề gì khi dùng Future ?
- Các message mà Actor nhận được được xếp hàng đợi trong hộp thư của Acotr và được xử lý từng message một.
- Việc sử dụng Future trong mô hình Actor sẽ phá vỡ sự đóng gói của Actor. Vì Future không đồng bộ, Actor có thể bắt đầu xử lý một số Message tiếp theo trong khi Future được liên kết với message trước đó vẫn đang chạy. Điều này sẽ có thể gây ra các lỗi khó kiểm soát.
Pipe pattern giải quyết vấn đề đó như thế nào?
- Pipe sẽ nhận các message Future bất đồng bộ và bắn lại cho chính nó hoặc Actor khác. Việc sử dụng Pỉpe sẽ làm cho các message bất đồng bộ được xếp trong hàng đợi của chính nó hoặc trong các Actor khác => các Future sẽ xử lý tuần tự và chính xác.
