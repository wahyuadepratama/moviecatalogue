package com.github.wahyuadepratama.whatsmovie.database.tvshow;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.github.wahyuadepratama.whatsmovie.database.helper.GenreConverter;
import com.github.wahyuadepratama.whatsmovie.database.helper.VideoConverter;
import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;
import com.github.wahyuadepratama.whatsmovie.model.video.Video;

import java.util.List;

@Entity(tableName = "tvshow_detail")
public class TVShowDetailTable {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "number_of_episodes")
    public int number_of_episodes;

    @TypeConverters(GenreConverter.class)
    @ColumnInfo(name = "genre")
    public List<Genre> genre;

    @TypeConverters(VideoConverter.class)
    @ColumnInfo(name = "video")
    public List<Video> video;
}
