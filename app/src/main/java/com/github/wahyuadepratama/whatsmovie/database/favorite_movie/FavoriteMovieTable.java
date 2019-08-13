package com.github.wahyuadepratama.whatsmovie.database.favorite_movie;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movie")
public class FavoriteMovieTable {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "overview")
    public String overview;

    @ColumnInfo(name = "vote_average")
    public double vote_average;

    @ColumnInfo(name = "poster_path")
    public String poster_path;

    @ColumnInfo(name = "backdrop_path")
    public String backdrop_path;

    @ColumnInfo(name = "popularity")
    public double popularity;

    @ColumnInfo(name = "release_date")
    public String release_date;

    @ColumnInfo(name = "vote_count")
    public int vote_count;
}
