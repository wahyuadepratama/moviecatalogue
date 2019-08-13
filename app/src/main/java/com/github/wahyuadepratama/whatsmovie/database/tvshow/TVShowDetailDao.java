package com.github.wahyuadepratama.whatsmovie.database.tvshow;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TVShowDetailDao {
    @Query("SELECT * FROM tvshow_detail WHERE id = :id")
    TVShowDetailTable getDataTVShowDetail(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTVShow(TVShowDetailTable tvshow);
}
