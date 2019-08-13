package com.github.wahyuadepratama.favoritemovie.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.wahyuadepratama.favoritemovie.R;
import com.github.wahyuadepratama.favoritemovie.adapter.FavoriteMovieAdapter;
import com.github.wahyuadepratama.favoritemovie.model.Movie;
import com.github.wahyuadepratama.favoritemovie.utils.Static;

import java.util.ArrayList;

import static com.github.wahyuadepratama.favoritemovie.utils.Static.LOADER;
import static com.github.wahyuadepratama.favoritemovie.utils.Static.URI_MOVIE;

public class MainActivity extends AppCompatActivity implements FavoriteMovieAdapter.OnMovieItemClicked{

    private SwipeRefreshLayout SwipeRefresh;
    private RecyclerView rvMovie;
    private FavoriteMovieAdapter listFavoriteMovieAdapter;
    private ArrayList<Movie> listFavoriteMovie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovie = findViewById(R.id.rv_favorite_movie);
        onConfigurationChanged(new Configuration());
        showSwipeRefresh();

        listFavoriteMovieAdapter = new FavoriteMovieAdapter(this);
        listFavoriteMovieAdapter.setListMovie(listFavoriteMovie);
        listFavoriteMovieAdapter.setClickHandler(this);
        getSupportLoaderManager().initLoader(LOADER,null, mLoaderCallbacks);
    }

    private void showSwipeRefresh(){
        SwipeRefresh = findViewById(R.id.swipe_refresh);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefresh.setRefreshing(false);
                        getSupportLoaderManager().initLoader(LOADER,null, mLoaderCallbacks);
                    }
                },3000);
            }
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
            rvMovie.setLayoutManager(new GridLayoutManager(this, 5));
        else
            rvMovie.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER) {
                return new CursorLoader(getApplicationContext(),
                        URI_MOVIE,
                        new String[]{"favorite_movie"},
                        null, null, null);
            }
            throw new IllegalArgumentException();

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loader.getId() == LOADER) {
                ArrayList<Movie> movies = new ArrayList<>();
                for (int i=0; i<data.getCount(); i++){
                    data.moveToPosition(i);
                    Movie movie = new Movie(data);
                    movies.add(movie);
                }
                listFavoriteMovieAdapter.setListMovie(movies);
                rvMovie.setAdapter(listFavoriteMovieAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loader.getId() == LOADER) {
                listFavoriteMovieAdapter.setListMovie(null);
            }
        }

    };

    @Override
    public void movieItemClicked(Movie m) {
        Intent detailMovieActivityIntent = new Intent(this, MovieDetailActivity.class);
        detailMovieActivityIntent.putExtra(Static.EXTRA_MOVIES, m);
        startActivity(detailMovieActivityIntent);
    }
}
