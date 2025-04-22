package com.example.hoang_movie.favorites;

import com.example.hoang_movie.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
@Module(includes = AppModule.class) // Import các providers từ AppModule. Tức là module này phụ thuộc vào AppModule.
public class FavoritesModule {
    @Provides //đây là cách tạo ra một FavoritesInteractor
    @Singleton
    FavoritesInteractor provideFavouritesInteractor(FavoritesStore store){
        //Dagger sẽ tự động cung cấp FavoritesStore nếu đã biết cách tạo nó (ví dụ đã được cung cấp từ AppModule).
        //Sau đó, Dagger sẽ gọi constructor new FavoritesInteractorImpl(store) để tạo FavoritesInteractor.
        return new FavoritesInteractorImpl(store);
    }
}
