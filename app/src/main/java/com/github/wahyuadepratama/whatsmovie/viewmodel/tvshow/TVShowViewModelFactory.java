package com.github.wahyuadepratama.whatsmovie.viewmodel.tvshow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TVShowViewModelFactory implements ViewModelProvider.Factory {

    private String mStatus;

    public TVShowViewModelFactory(String status) {
        mStatus = status;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TVShowViewModel(mStatus);
    }
}
