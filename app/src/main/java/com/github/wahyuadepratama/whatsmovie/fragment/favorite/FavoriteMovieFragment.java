package com.github.wahyuadepratama.whatsmovie.fragment.favorite;


import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.activity.MovieDetailActivity;
import com.github.wahyuadepratama.whatsmovie.adapter.FavoriteMovieAdapter;
import com.github.wahyuadepratama.whatsmovie.database.favorite_movie.FavoriteMovieTable;
import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.github.wahyuadepratama.whatsmovie.viewmodel.favorite.FavoriteMovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements FavoriteMovieAdapter.OnMovieItemClicked {

    private RecyclerView rvMovie;
    private FavoriteMovieAdapter listFavoriteMovieAdapter;
    private ArrayList<Movie> listFavoriteMovie = new ArrayList<>();
    private SwipeRefreshLayout SwipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        rvMovie = view.findViewById(R.id.rv_favorite_movie);

        showSwipeRefresh(view);
        showRecyclerListMovie();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FavoriteMovieViewModel movieViewModel= ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        movieViewModel.getDataFromDB(new Application());
        showRecyclerListMovie();
    }

    private void showSwipeRefresh(View view){
        SwipeRefresh = view.findViewById(R.id.swipe_refresh);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefresh.setRefreshing(false);
                        showRecyclerListMovie();
                    }
                },3000);
            }
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = view.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
            rvMovie.setLayoutManager(new GridLayoutManager(getContext(), 5));
        else
            rvMovie.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    private void showRecyclerListMovie(){
        listFavoriteMovieAdapter = new FavoriteMovieAdapter(getContext());
        listFavoriteMovieAdapter.setListMovie(listFavoriteMovie);
        listFavoriteMovieAdapter.setClickHandler(this);
        rvMovie.setAdapter(listFavoriteMovieAdapter);

        onConfigurationChanged(new Configuration());
        retrieveTasks();
    }

    private void retrieveTasks() {
        FavoriteMovieViewModel movieViewModel= ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        movieViewModel.getFavoriteMovieListLiveData().observe(this, new Observer<List<FavoriteMovieTable>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovieTable> listMovie) {
                List<Movie> movieItemList = new ArrayList<>();
                for(FavoriteMovieTable favoriteMovieTable : Objects.requireNonNull(listMovie)){
                    Movie m = new Movie(
                            favoriteMovieTable.id,
                            favoriteMovieTable.title,
                            favoriteMovieTable.overview,
                            favoriteMovieTable.vote_average,
                            favoriteMovieTable.poster_path,
                            favoriteMovieTable.backdrop_path,
                            favoriteMovieTable.popularity,
                            favoriteMovieTable.release_date,
                            favoriteMovieTable.vote_count
                    );
                    movieItemList.add(m);
                }
                listFavoriteMovieAdapter.setListMovie(new ArrayList<>(movieItemList));
                checkStatusSavedFavoriteItem();
            }
        });
    }

    private void checkStatusSavedFavoriteItem(){
        TextView notification = view.findViewById(R.id.txt_info_favorite);
        if (listFavoriteMovieAdapter.getItemCount() == 0)
            notification.setVisibility(View.VISIBLE);
        else
            notification.setVisibility(View.INVISIBLE);
    }

    @Override
    public void movieItemClicked(Movie m) {
        Intent detailMovieActivityIntent = new Intent(getActivity(), MovieDetailActivity.class);
        detailMovieActivityIntent.putExtra(Interfaces.EXTRA_MOVIES, m);
        startActivity(detailMovieActivityIntent);
    }
}
