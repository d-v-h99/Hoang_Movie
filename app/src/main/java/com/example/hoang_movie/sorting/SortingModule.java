package com.example.hoang_movie.sorting;

import dagger.Module;
import dagger.Provides;

@Module
public class SortingModule {
    @Provides
    SortingDialogInteractor providerSortingDialogInteractor(SortingOptionStore store){
        return new SortingDialogInteractorImpl(store);
    }
    @Provides
    SortingDialogPresenter providePresenter(SortingDialogInteractor interactor) {
        return new SortingDialogPresenterImpl(interactor);
    }
}
