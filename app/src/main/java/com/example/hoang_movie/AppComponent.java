package com.example.hoang_movie;

import com.example.hoang_movie.details.DetailsComponent;
import com.example.hoang_movie.details.DetailsModule;
import com.example.hoang_movie.favorites.FavoritesModule;
import com.example.hoang_movie.listing.ListingComponent;
import com.example.hoang_movie.listing.ListingModule;
import com.example.hoang_movie.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        FavoritesModule.class
})
public interface AppComponent {
    DetailsComponent plus(DetailsModule detailsModule);

    ListingComponent plus(ListingModule listingModule);
}
