package com.github.wahyuadepratama.whatsmovie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.fragment.favorite.PlaceholderFavoriteFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.home.HomeFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.movie.MovieFragment;
import com.github.wahyuadepratama.whatsmovie.fragment.tvshow.TVShowFragment;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements Interfaces.BottomMenuManager {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    editor.putInt(Interfaces.STATUS_MENU, Interfaces.MENU_HOME_SELECTED).apply();
                    setFragmentToHome(new HomeFragment());
                    return true;
                case R.id.navigation_movie:
                    editor.putInt(Interfaces.STATUS_MENU, Interfaces.MENU_MOVIES_SELECTED).apply();
                    setFragmentToMovie(new MovieFragment());
                    return true;
                case R.id.navigation_tv:
                    editor.putInt(Interfaces.STATUS_MENU, Interfaces.MENU_TV_SHOW_SELECTED).apply();
                    setFragmentToTVShow(new TVShowFragment());
                    return true;
                case R.id.navigation_favorite:
                    editor.putInt(Interfaces.STATUS_MENU, Interfaces.MENU_FAVORITE_SELECTED).apply();
                    setFragmentToFavorite(new PlaceholderFavoriteFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFloatingActionButton();
        checkPreference(navView);
    }

    private void showFloatingActionButton(){
        FloatingActionButton language = findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            }
        });
        FloatingActionButton refreshApp= findViewById(R.id.refresh_app);
        refreshApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        FloatingActionButton settingApp= findViewById(R.id.notification);
        settingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailMovieActivityIntent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(detailMovieActivityIntent);
            }
        });
    }

    @Override
    public void checkPreference(BottomNavigationView navView){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int mode = preferences.getInt(Interfaces.STATUS_MENU, Interfaces.MENU_HOME_SELECTED); //default value is menu_movies_selected

        switch (mode){
            case(Interfaces.MENU_HOME_SELECTED):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                setFragmentToHome(new HomeFragment());
                break;
            case(Interfaces.MENU_MOVIES_SELECTED):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                navView.getMenu().findItem(R.id.navigation_movie).setChecked(true);
                setFragmentToMovie(new MovieFragment());
                break;
            case(Interfaces.MENU_TV_SHOW_SELECTED):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                navView.getMenu().findItem(R.id.navigation_tv).setChecked(true);
                setFragmentToTVShow(new TVShowFragment());
                break;
            case(Interfaces.MENU_FAVORITE_SELECTED):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                navView.getMenu().findItem(R.id.navigation_favorite).setChecked(true);
                setFragmentToFavorite(new PlaceholderFavoriteFragment());
                break;
        }
    }

    @Override
    public void setFragmentToHome(HomeFragment homeFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, homeFragment)
                .commit();
    }

    @Override
    public void setFragmentToMovie(MovieFragment movieFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, movieFragment)
                .commit();
    }

    @Override
    public void setFragmentToTVShow(TVShowFragment listTVShowFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, listTVShowFragment)
                .commit();
    }

    @Override
    public void setFragmentToFavorite(PlaceholderFavoriteFragment listFavoriteFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, listFavoriteFragment)
                .commit();
    }
}
