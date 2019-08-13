package com.github.wahyuadepratama.whatsmovie.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.wahyuadepratama.whatsmovie.database.favorite_movie.FavoriteMovieTable;
import com.github.wahyuadepratama.whatsmovie.database.favorite_movie.FavoriteMovieDao;
import com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow.FavoriteTVShowTable;
import com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow.FavoriteTVShowDao;
import com.github.wahyuadepratama.whatsmovie.database.movie.MovieDetailDao;
import com.github.wahyuadepratama.whatsmovie.database.movie.MovieDetailTable;
import com.github.wahyuadepratama.whatsmovie.database.tvshow.TVShowDetailDao;
import com.github.wahyuadepratama.whatsmovie.database.tvshow.TVShowDetailTable;

@Database( entities = {FavoriteMovieTable.class, FavoriteTVShowTable.class, MovieDetailTable.class, TVShowDetailTable.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    @SuppressWarnings("WeakerAccess")
    public abstract FavoriteMovieDao favoriteMovieDao();
    public abstract FavoriteTVShowDao favoriteTVShowDao();
    public abstract MovieDetailDao movieDetailDao();
    public abstract TVShowDetailDao tvShowDetailDao();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "tmdb.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

}
