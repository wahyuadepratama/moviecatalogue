package com.github.wahyuadepratama.whatsmovie.database.movie;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDetailDao {

    @Query("SELECT * FROM movie_detail WHERE id = :id")
    MovieDetailTable getDataMovieDetail(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieDetailTable movie);
}
