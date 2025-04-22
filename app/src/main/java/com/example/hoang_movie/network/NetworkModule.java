package com.example.hoang_movie.network;

import com.example.hoang_movie.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class NetworkModule {
    public static final int CONNECT_TIMEOUT_IN_MS = 30000;
    @Provides
    @Singleton
    //ết trong @Module: tự tay cung cấp object, thường dùng khi cần logic phức tạp hoặc bên ngoài Dagger quản lý (lib không thể gắn @Inject)
    //Để Dagger hiểu rằng khi ai đó cần một Interceptor, thì hãy cung cấp RequestInterceptor như một instance của Interceptor.
    Interceptor requestInterceptor(RequestInterceptor interceptor){
        return interceptor;
    }
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(RequestInterceptor requestInterceptor){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(requestInterceptor);
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }
    /* App cần gọi API
   ↓
Retrofit cần OkHttpClient
   ↓
OkHttpClient được tạo bởi provideOkHttpClient()
   ↳ Dùng RequestInterceptor để tự động gắn api_key
   ↳ Dùng LoggingInterceptor để log HTTP
 Loại	Mục đích
application interceptor	Gắn bằng .addInterceptor() – can thiệp toàn bộ request
network interceptor	Gắn bằng .addNetworkInterceptor() – can thiệp request sau khi đi qua cache, DNS...
*/
    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())// convert JSON → Object Java (giống như Gson, nhưng tối ưu hơn và có annotation tốt hơn). Giúp Retrofit hiểu cách parse JSON thành MoviesWrapper, Movie, VideoWrapper...
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())//it trả về kiểu Observable<T> thay vì Call<T>.
                .client(okHttpClient)
                .build();
    }
    @Singleton
    @Provides
    TmdbWebService tmdbWebService(Retrofit retrofit){
        return  retrofit.create(TmdbWebService.class);
    }
    /*
    * 1. Dagger thấy bạn cần TmdbWebService
2. Để tạo TmdbWebService → cần Retrofit
3. Để tạo Retrofit → cần OkHttpClient
4. Để tạo OkHttpClient → cần RequestInterceptor
5. Để tạo RequestInterceptor → gọi constructor có @Inject

→ Tất cả đều rõ ràng, không vòng lặp
* @Inject constructor → Dagger biết cách tạo object cụ thể
@Provides → Dagger biết ánh xạ giữa loại abstract (interface) và concrete (class)

*/
}
