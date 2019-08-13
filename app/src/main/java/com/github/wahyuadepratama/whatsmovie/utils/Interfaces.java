package com.github.wahyuadepratama.whatsmovie.utils;

import com.github.wahyuadepratama.whatsmovie.fragment.favorite.PlaceholderFavoriteFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.home.HomeFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.movie.MovieFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.tvshow.TVShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Interfaces {
    //API
    public static final String URL_TM_DB = "https://api.themoviedb.org/";
    public static final String URL_IMG_TM_DB = "http://image.tmdb.org/t/p/w500";
    public static final String URL_IMG_TM_DB_ORIGINAL = "http://image.tmdb.org/t/p/original";
    public static final String API_KEY = "ed7818e67c85a6ce60d701cde5cbcff2";

    //Parcelable in Detail Activity
    public static final String EXTRA_MOVIES = "movies";
    public static final String EXTRA_TV_SHOW = "tvShow";

    //Status Menu in HomeActivity - SharedPreferences
    public static final String STATUS_MENU = "status_menu";
    public static final int MENU_HOME_SELECTED = 0;
    public static final int MENU_MOVIES_SELECTED = 1;
    public static final int MENU_TV_SHOW_SELECTED = 2;
    public static final int MENU_FAVORITE_SELECTED = 3;

    //Status sort_by in movie fragment
    public static final String UPCOMING_MOVIE = "upcoming_movie";
    public static final String NOW_PLAYING_MOVIE = "now_playing_movie";
    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String SEARCH_MOVIE = "search_movie";

    //Status sort_by in tv show fragment
    public static final String POPULARITY_TV = "popularity.desc";
    public static final String SEARCH_TV = "search_tv_show";

    //Status for search View by SPEECH
    public static final int RESULT_SPEECH_MOVIE = 1;
    public static final int RESULT_SPEECH_TVSHOW = 1;

    //Status for setting
    public static final String SETTING_REMINDER_APPLICATION = "daily reminder";
    public static final String SETTING_REMINDER_MOVIE = "daily reminder movie";
    public static final int STATUS_SETTING_COMEBACK_ACTIVE = 10;
    public static final int STATUS_SETTING_MOVIES_ACTIVE = 11;
    public static final int STATUS_SETTING_NON_ACTIVE = 0;

    public interface BottomMenuManager {
        void checkPreference(BottomNavigationView navView);
        void setFragmentToHome(HomeFragment homeFragment);
        void setFragmentToMovie(MovieFragment movieFragment);
        void setFragmentToTVShow(TVShowFragment listTVShowFragment);
        void setFragmentToFavorite(PlaceholderFavoriteFragment listFavoriteFragment);
    }

    public interface LoadingManager {
        Boolean isConnected();
        void startLoading();
        void stopLoading();
        void startErrorLoading();
    }
}
