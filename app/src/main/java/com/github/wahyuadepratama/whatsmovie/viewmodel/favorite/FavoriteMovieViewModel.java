package com.github.wahyuadepratama.whatsmovie.viewmodel.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.wahyuadepratama.whatsmovie.database.AppDatabase;
import com.github.wahyuadepratama.whatsmovie.database.favorite_movie.FavoriteMovieTable;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovieTable>> favoriteMovieListLiveData;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        getDataFromDB(application);
    }

    public void getDataFromDB(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        favoriteMovieListLiveData = appDatabase.favoriteMovieDao().getAllFavoriteMovie();
    }

    public LiveData<List<FavoriteMovieTable>> getFavoriteMovieListLiveData() {
        return favoriteMovieListLiveData;
    }
}
