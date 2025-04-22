package com.example.hoang_movie.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

public final class SimpleCountingIdlingResource implements IdlingResource {
    private final String mResourceName;
    private final AtomicInteger counter = new AtomicInteger(0);
    //Nếu counter == 0 → app đang rảnh (idle)
    //
    //Nếu counter > 0 → app đang bận (not idle)
    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String mResourceName) {
        this.mResourceName = checkNotNull(mResourceName);
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = resourceCallback;
    }
    public void increment() {
        counter.getAndIncrement();
    }
    public void decrement(){
        int counterVal = counter.decrementAndGet();
        if(counterVal == 0){
            if(null != resourceCallback){
                resourceCallback.onTransitionToIdle();
            }
        }
        if(counterVal <0){
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}
