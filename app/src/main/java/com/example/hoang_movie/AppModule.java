package com.example.hoang_movie;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class AppModule {
    private Context context;

    public AppModule(Application application) {
        this.context = application;
    }
    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }
    @Provides
    @Singleton
    public Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
