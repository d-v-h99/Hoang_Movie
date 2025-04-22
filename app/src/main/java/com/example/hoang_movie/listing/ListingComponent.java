package com.example.hoang_movie.listing;

import com.example.hoang_movie.sorting.SortingDialogFragment;
import com.example.hoang_movie.sorting.SortingModule;

import dagger.Subcomponent;

@ListingScope
@Subcomponent(modules = {SortingModule.class, ListingModule.class})
public interface ListingComponent {
    SortingDialogFragment inject(SortingDialogFragment fragment);
    MoviesListingFragment inject(MoviesListingFragment fragment);
}
 /*
 * Dagger Subcomponent
 * Nó đại diện cho một vùng phụ thuộc (dependency graph) có phạm vi riêng – được xác định bởi @ListingScope
 * Cung cấp các phụ thuộc (dependencies) cần thiết cho:

SortingDialogFragment

MoviesListingFragment

Thông qua các module:

SortingModule

ListingModule
* Đây là một Subcomponent – tức là một component con nằm trong một component cha (thường là AppComponent hoặc ApplicationComponent).
* Khi bạn gọi listingComponent.inject(fragment), Dagger sẽ:

tìm các trường @Inject hoặc constructor @Inject trong SortingDialogFragment hoặc MoviesListingFragment.

và tự động khởi tạo / gán các dependency đã được cấu hình từ module.*/