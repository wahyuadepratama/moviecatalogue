package com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteTVShowDao {

    @Query("SELECT * FROM favorite_tvshow")
    LiveData<List<FavoriteTVShowTable>> getAllFavoriteTVShow();

    @Query("SELECT * FROM favorite_tvshow WHERE id = :id")
    FavoriteTVShowTable getOneFavoriteTVShow(int id);

    @Query("DELETE FROM favorite_tvshow WHERE id = :id")
    void deleteOneFavoriteTVShow(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteTVShow(FavoriteTVShowTable favoriteTVShowTable);
}
