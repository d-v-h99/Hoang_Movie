package com.example.hoang_movie.utils;

import androidx.test.espresso.IdlingResource;

public class EspressoIdlingResource {
    public static final String RESOURCE = "GLOBAL";
    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);
    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
