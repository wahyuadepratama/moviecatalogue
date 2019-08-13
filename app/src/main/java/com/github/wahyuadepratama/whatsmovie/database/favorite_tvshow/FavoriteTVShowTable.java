package com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_tvshow")
public class FavoriteTVShowTable {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "overview")
    public String overview;

    @ColumnInfo(name = "vote_average")
    public double vote_average;

    @ColumnInfo(name = "poster_path")
    public String poster_path;

    @ColumnInfo(name = "backdrop_path")
    public String backdrop_path;

    @ColumnInfo(name = "first_air_date")
    public String first_air_date;

    @ColumnInfo(name = "vote_count")
    public int vote_count;

    @ColumnInfo(name = "popularity")
    public double popularity;
}
