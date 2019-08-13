package com.github.wahyuadepratama.whatsmovie.fragment.tvshow;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.activity.HomeActivity;
import com.github.wahyuadepratama.whatsmovie.activity.TVShowDetailActivity;
import com.github.wahyuadepratama.whatsmovie.adapter.TVShowAdapter;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.github.wahyuadepratama.whatsmovie.viewmodel.tvshow.TVShowViewModel;
import com.github.wahyuadepratama.whatsmovie.viewmodel.tvshow.TVShowViewModelFactory;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment implements TVShowAdapter.OnTVShowItemClicked, Interfaces.LoadingManager, MaterialSearchBar.OnSearchActionListener{

    private RecyclerView rvTVShow;
    private ArrayList<TVShow> listTVShow = new ArrayList<>();
    private TVShowAdapter listTVShowAdapter;
    private SwipeRefreshLayout SwipeRefresh;
    private View view;
    private Button topMenu;
    private MaterialSearchBar searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tvshow, container, false);

        showSwipeRefresh(view);
        showRecyclerListTVShow(Interfaces.POPULARITY_TV, getResources().getString(R.string.popularity_tv));
        customActionBar();
        actionBarMenuClicked();
        searchViewClicked();

        return view;
    }

    private void customActionBar(){
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Objects.requireNonNull(((HomeActivity) getActivity()).getSupportActionBar()).setCustomView(R.layout.abs_movie_tvshow_layout);
        Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setElevation(0);
        Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTransparent)));
        searchBar = ((HomeActivity) Objects.requireNonNull(getActivity())).findViewById(R.id.search_custom);
        try {
            final Field placeHolder = searchBar.getClass().getDeclaredField("placeHolder");
            placeHolder.setAccessible(true);
            final TextView textView = (TextView) placeHolder.get(searchBar);
            Typeface typeface = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.quicksand_medium);
            //<string name="roboto_medium">fonts/Roboto-Medium.ttf</string>
            textView.setTypeface(typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        showRecyclerListTVShow(Interfaces.SEARCH_TV, text.toString());
        refreshOnClickActionBar();
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_SPEECH:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(intent, Interfaces.RESULT_SPEECH_MOVIE);
                    searchBar.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Interfaces.RESULT_SPEECH_TVSHOW) {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                searchBar.setText(text.get(0));
                showRecyclerListTVShow(Interfaces.SEARCH_TV, text.get(0));
                refreshOnClickActionBar();
            }
        }
    }

    private void actionBarMenuClicked(){
        topMenu = ((HomeActivity) Objects.requireNonNull(getActivity())).findViewById(R.id.btn_top_menu);
        topMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), topMenu);
                popup.getMenuInflater().inflate(R.menu.menu_top_tv, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.popular_tv){
                            showRecyclerListTVShow(Interfaces.POPULARITY_TV, getResources().getString(R.string.popularity_tv));
                            refreshOnClickActionBar();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    private void searchViewClicked(){
        searchBar = ((HomeActivity) Objects.requireNonNull(getActivity())).findViewById(R.id.search_custom);
        searchBar.setSpeechMode(true);
        searchBar.setOnSearchActionListener(this);
        searchBar.setCardViewElevation(10);

        rvTVShow.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
                } else if (dy < -1) {
                    Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
                    Objects.requireNonNull(((HomeActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTransparent)));
                }
            }
        });
    }

    private void showSwipeRefresh(View view){
        SwipeRefresh = view.findViewById(R.id.swipe_refresh);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setProgressViewOffset(false, 0, 150);

        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefresh.setRefreshing(false);
                        showRecyclerListTVShow(Interfaces.POPULARITY_TV, getResources().getString(R.string.popularity_tv));

                    }
                },3000);
            }
        });
    }

    private void refreshOnClickActionBar(){
        SwipeRefresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SwipeRefresh.setRefreshing(false);
            }
        },5000);
    }

    private void showRecyclerListTVShow(String status, String query){
        rvTVShow = view.findViewById(R.id.rv_tv);
        listTVShowAdapter = new TVShowAdapter(getContext());
        listTVShowAdapter.setListTVShow(listTVShow);
        listTVShowAdapter.setClickHandler(this);
        rvTVShow.setAdapter(listTVShowAdapter);

        startLoading();
        onConfigurationChanged(new Configuration());

        retrieveTVShowViewModel(status, query);
    }

    private void retrieveTVShowViewModel(String status, final String query) {
        if(getActivity() != null){
            TVShowViewModel myViewModel = ViewModelProviders.of(getActivity(), new TVShowViewModelFactory(status)).get(TVShowViewModel.class);

            if(status.equals(Interfaces.POPULARITY_TV))
                myViewModel.getDataDiscover(Interfaces.POPULARITY_TV);
            if(status.equals(Interfaces.SEARCH_TV))
                myViewModel.getDataSearch(query);

            myViewModel.getListTVShow().observe(this, new Observer<ArrayList<TVShow>>() {
                @Override
                public void onChanged(@Nullable ArrayList<TVShow> tvShow) {
                    listTVShowAdapter.setListTVShow(tvShow);
                    listTVShowAdapter.setSort_by(query);
                    if (listTVShowAdapter.getItemCount() == 0){
                        Toast.makeText(getContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                    }
                    stopLoading();
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = view.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
            rvTVShow.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else
            rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void tvShowItemClicked(TVShow m) {
        Intent detailTVShowActivityIntent = new Intent(getActivity(), TVShowDetailActivity.class);
        detailTVShowActivityIntent.putExtra(Interfaces.EXTRA_TV_SHOW, m);
        startActivity(detailTVShowActivityIntent);
    }

    @Override
    public Boolean isConnected() {
        if (getContext() != null){
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
        }else{
            return true;
        }
    }

    @Override
    public void startLoading() {
        TextView loading = view.findViewById(R.id.loading);
        LottieAnimationView waiting = view.findViewById(R.id.lav_waiting);

        SwipeRefresh.setRefreshing(true);
        waiting.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void stopLoading() {
        TextView loading = view.findViewById(R.id.loading);
        LottieAnimationView waiting = view.findViewById(R.id.lav_waiting);

        SwipeRefresh.setRefreshing(false);
        waiting.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startErrorLoading() {
        TextView loading = view.findViewById(R.id.loading);
        LottieAnimationView waiting = view.findViewById(R.id.lav_waiting);

        SwipeRefresh.setRefreshing(false);
        waiting.setVisibility(View.VISIBLE);
        waiting.playAnimation();
        loading.setVisibility(View.VISIBLE);
        loading.setText(getResources().getString(R.string.connection_error));
    }
}
