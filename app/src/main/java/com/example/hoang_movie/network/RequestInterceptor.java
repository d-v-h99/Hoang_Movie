package com.example.hoang_movie.network;

import androidx.annotation.NonNull;

import com.example.hoang_movie.BuildConfig;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/*
* NetworkModule
    ├── cung cấp OkHttpClient (cần RequestInterceptor)
    ├── cung cấp Retrofit (cần OkHttpClient)
    └── cung cấp TmdbWebService (cần Retrofit)

RequestInterceptor
    └── Được Dagger tự tạo nhờ constructor @Inject
*/
public class RequestInterceptor implements Interceptor {
    //@Inject	Gắn vào constructor: nói cho Dagger biết cách tạo object nếu không có @Provides
    //Khi Dagger cần tạo RequestInterceptor, nó có thể tự động gọi constructor này mà bạn không cần viết @Provides.
    //Nó có constructor mặc định có annotation @Inject.
    //Không có dependency nào khác cần truyền vào → Dagger có thể tạo trực tiếp
    @Inject
    public RequestInterceptor() {
    }
    //Phương thức intercept() trong class RequestInterceptor của bạn là phần cốt lõi trong kiến trúc OkHttp Interceptor, dùng để can thiệp và chỉnh sửa HTTP request hoặc response trước khi gửi / sau khi nhận.
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();
        Request request = original.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
