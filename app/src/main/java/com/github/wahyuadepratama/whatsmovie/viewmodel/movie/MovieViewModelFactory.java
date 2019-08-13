package com.github.wahyuadepratama.whatsmovie.viewmodel.movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieViewModelFactory implements ViewModelProvider.Factory {

    private String mStatus;

    public MovieViewModelFactory(String status) {
        mStatus = status;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mStatus);
    }
}
