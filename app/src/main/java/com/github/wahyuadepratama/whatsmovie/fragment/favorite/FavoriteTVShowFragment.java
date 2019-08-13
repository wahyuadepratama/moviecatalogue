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
import com.github.wahyuadepratama.whatsmovie.activity.TVShowDetailActivity;
import com.github.wahyuadepratama.whatsmovie.adapter.FavoriteTVShowAdapter;
import com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow.FavoriteTVShowTable;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.github.wahyuadepratama.whatsmovie.viewmodel.favorite.FavoriteTVShowViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTVShowFragment extends Fragment implements FavoriteTVShowAdapter.OnTVShowItemClicked {

    private RecyclerView rvMovie;
    private FavoriteTVShowAdapter listFavoriteTVShowAdapter;
    private ArrayList<TVShow> listFavoriteMovie = new ArrayList<>();
    private SwipeRefreshLayout SwipeRefresh;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);
        rvMovie = view.findViewById(R.id.rv_favorite_tv);

        showSwipeRefresh(view);
        showRecyclerListTVShow();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FavoriteTVShowViewModel tvShowViewModel= ViewModelProviders.of(this).get(FavoriteTVShowViewModel.class);
        tvShowViewModel.getDataFromDB(new Application());
        showRecyclerListTVShow();
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
                        showRecyclerListTVShow();
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

    private void showRecyclerListTVShow(){
        listFavoriteTVShowAdapter = new FavoriteTVShowAdapter(getContext());
        listFavoriteTVShowAdapter.setListTVShow(listFavoriteMovie);
        listFavoriteTVShowAdapter.setClickHandler(this);
        rvMovie.setAdapter(listFavoriteTVShowAdapter);

        onConfigurationChanged(new Configuration());
        retrieveTasks();
    }

    private void retrieveTasks() {
        FavoriteTVShowViewModel tvShowViewModel= ViewModelProviders.of(this).get(FavoriteTVShowViewModel.class);
        tvShowViewModel.getFavoriteTVShowListLiveData().observe(this, new Observer<List<FavoriteTVShowTable>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteTVShowTable> listTVShow) {
                List<TVShow> tvShowItemList = new ArrayList<>();
                for(FavoriteTVShowTable favoriteTVShowTable : Objects.requireNonNull(listTVShow)){
                    TVShow m = new TVShow(
                            favoriteTVShowTable.id,
                            favoriteTVShowTable.name,
                            favoriteTVShowTable.overview,
                            favoriteTVShowTable.vote_average,
                            favoriteTVShowTable.poster_path,
                            favoriteTVShowTable.backdrop_path,
                            favoriteTVShowTable.first_air_date,
                            favoriteTVShowTable.vote_count,
                            favoriteTVShowTable.popularity
                    );
                    tvShowItemList.add(m);
                }
                listFavoriteTVShowAdapter.setListTVShow(new ArrayList<>(tvShowItemList));
                checkStatusSavedFavoriteItem();
            }
        });
    }

    private void checkStatusSavedFavoriteItem(){
        TextView notification = view.findViewById(R.id.txt_info_favorite);
        if (listFavoriteTVShowAdapter.getItemCount() == 0)
            notification.setVisibility(View.VISIBLE);
        else
            notification.setVisibility(View.INVISIBLE);
    }

    @Override
    public void tvShowItemClicked(TVShow m) {
        Intent detailTVShowActivityIntent = new Intent(getActivity(), TVShowDetailActivity.class);
        detailTVShowActivityIntent.putExtra(Interfaces.EXTRA_TV_SHOW, m);
        startActivity(detailTVShowActivityIntent);
    }
}
