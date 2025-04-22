package com.example.hoang_movie.utils;

import io.reactivex.rxjava3.disposables.Disposable;

public class RxUtils {
    public static void unsubscribe(Disposable subscription){
        if(subscription != null && !subscription.isDisposed()){
            subscription.dispose();
        }
    }
    //varargs (variable-length arguments), tức là tham số đầu vào có thể nhận 0, 1 hoặc nhiều giá trị mà không cần phải tạo mảng thủ công.
    public static void unsubscribe(Disposable... subscriptions){
        for(Disposable subscription: subscriptions){
            unsubscribe(subscription);
        }
    }
}
