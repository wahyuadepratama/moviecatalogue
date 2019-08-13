package com.github.wahyuadepratama.favoritemovie.utils;

import android.net.Uri;

public class Static {
    public static final String URL_IMG_TM_DB = "http://image.tmdb.org/t/p/w500";

    public static final String AUTHORITY = "com.github.wahyuadepratama.whatsmovie.provider";

    public static final Uri URI_MOVIE = Uri.parse("content://" + AUTHORITY + "/" + "favorite_movie");
    public static final int LOADER = 1;

    public static final String EXTRA_MOVIES = "movies";
}
