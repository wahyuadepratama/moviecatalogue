package com.github.wahyuadepratama.whatsmovie.database.favorite_movie;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie")
    LiveData<List<FavoriteMovieTable>> getAllFavoriteMovie();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    FavoriteMovieTable getOneFavoriteMovie(int id);

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    void deleteOneFavoriteMovie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovieTable favoriteMovieTable);

    @Query("SELECT * FROM favorite_movie")
    List<Movie> readAllFavoriteMovie();

    @Query("SELECT * FROM favorite_movie")
    Cursor getAllFavoriteMovieWithCursor();
}
