package com.example.hoang_movie.listing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ListingScope {
}
/*Tạo phạm vi tiêm phụ thuộc
* Retention(RetentionPolicy.RUNTIME)
Chỉ định thời điểm giữ lại annotation – trong trường hợp này là runtime.

Dagger cần annotation này tồn tại lúc chạy để hoạt động đúng.
* rong một app, bạn có thể có nhiều vùng logic – ví dụ:

@Singleton: cho toàn bộ vòng đời của app.

@ActivityScope: cho mỗi activity (nếu bạn define nó).

@ListingScope: chỉ dùng cho các dependencies liên quan đến phần "listing" (ví dụ: danh sách phim, danh sách sản phẩm...). */