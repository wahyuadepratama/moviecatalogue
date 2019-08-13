package com.github.wahyuadepratama.whatsmovie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.room.Room;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.database.AppDatabase;
import com.github.wahyuadepratama.whatsmovie.database.favorite_tvshow.FavoriteTVShowTable;
import com.github.wahyuadepratama.whatsmovie.database.tvshow.TVShowDetailTable;
import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShowDetail;
import com.github.wahyuadepratama.whatsmovie.model.video.Video;
import com.github.wahyuadepratama.whatsmovie.model.video.VideoList;
import com.github.wahyuadepratama.whatsmovie.utils.ApiClient;
import com.github.wahyuadepratama.whatsmovie.utils.GlideApp;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVShowDetailActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private List<Video> dataVideo = new ArrayList<>();
    private List<Genre> dataGenre = new ArrayList<>();
    private int runtime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        ImageView imgBackdrop = findViewById(R.id.img_detail_backdrop);
        ImageView bg = findViewById(R.id.bg);
        ImageView imgPoster = findViewById(R.id.img_detail_poster);
        TextView textDescription = findViewById(R.id.txt_detail_description);
        TextView textTitle = findViewById(R.id.txt_detail_title);
        Button btnRelease = findViewById(R.id.btn_detail_release);
        TextView txtReview = findViewById(R.id.txt_detail_review);
        TextView txtPopularity = findViewById(R.id.txt_detail_popularity);
        TextView txtRating = findViewById(R.id.txt_rating);
        TextView txtRuntime = findViewById(R.id.txt_detail_runtime);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.trailer);
        getLifecycle().addObserver(youTubePlayerView);
        AppCompatRatingBar ratingBar = findViewById(R.id.rt_bar);

        mDb = Room.databaseBuilder(this, AppDatabase.class, "tmdb.db")
                .allowMainThreadQueries()
                .build();

        checkFavoriteButton();

        Intent intent = getIntent();
        if(intent != null){
            final TVShow tvShow = intent.getParcelableExtra(Interfaces.EXTRA_TV_SHOW);

            textTitle.setText(tvShow.getName());
            textDescription.setText(String.valueOf(tvShow.getOverview()));
            btnRelease.setText(tvShow.getFirst_air_date());
            txtReview.setText(tvShow.getVote_count() + "\nratings");
            txtPopularity.setText(tvShow.getPopularity() + "\nviewers");
            ratingBar.setRating((float)tvShow.getVote_average());
            txtRating.setText(String.valueOf(tvShow.getVote_average()));
            GlideApp.with(this)
                    .load(Interfaces.URL_IMG_TM_DB_ORIGINAL + tvShow.getBackdrop_path())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.loading_backdrop)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fallback(R.drawable.loading_backdrop)
                    .error(R.drawable.loading_backdrop)
                    .apply(new RequestOptions())
                    .into(imgBackdrop);
            GlideApp.with(this)
                    .load(Interfaces.URL_IMG_TM_DB + tvShow.getPoster_path())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.loading_poster)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fallback(R.drawable.loading_poster)
                    .error(R.drawable.loading_poster)
                    .apply(new RequestOptions())
                    .into(imgPoster);
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
                GlideApp.with(this)
                        .load(Interfaces.URL_IMG_TM_DB + tvShow.getBackdrop_path())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.loading_poster)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fallback(R.drawable.loading_poster)
                        .error(R.drawable.loading_poster)
                        .apply(new RequestOptions())
                        .into(bg);
            } else{
                GlideApp.with(this)
                        .load(Interfaces.URL_IMG_TM_DB + tvShow.getPoster_path())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.loading_poster)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fallback(R.drawable.loading_poster)
                        .error(R.drawable.loading_poster)
                        .apply(new RequestOptions())
                        .into(bg);
            }

            TVShowDetailTable data = mDb.tvShowDetailDao().getDataTVShowDetail(tvShow.getId());
            if (data == null){
                getRuntimeAndGenreFromServer(tvShow.getId(), txtRuntime);
                getVideoFromServer(tvShow.getId());
                setRuntimeAndGenreToView(txtRuntime);
                videoListener(youTubePlayerView);
            }else{
                if (data.genre.isEmpty()) {
                    getRuntimeAndGenreFromServer(tvShow.getId(), txtRuntime);
                }else{
                    dataGenre = data.genre;
                    runtime = data.number_of_episodes;
                    setRuntimeAndGenreToView(txtRuntime);
                }

                if(data.video.isEmpty()){
                    getVideoFromServer(tvShow.getId());
                }else{
                    dataVideo = data.video;
                }
                videoListener(youTubePlayerView);
            }
        }
    }

    private void getVideoFromServer(final int tvShow_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );
                ApiClient client = builder.client(httpClient.build()).build().create(ApiClient.class);
                Call<VideoList> call = client.getTVShowVideo(tvShow_id, Interfaces.API_KEY);
                call.enqueue(new Callback<VideoList>() {
                    @Override
                    public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                        VideoList list = response.body();
                        dataVideo = Objects.requireNonNull(list).results;
                    }

                    @Override
                    public void onFailure(Call<VideoList> call, Throwable t) {
                        Log.d("Failed", "onFailure: "+t);
                    }
                });
            }
        }).start();
    }

    private void videoListener(YouTubePlayerView youTubePlayerView){
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if (!dataVideo.isEmpty()){

                    TextView textTrailerName = findViewById(R.id.txt_trailer_name);
                    TextView textTrailerSite = findViewById(R.id.txt_trailer_site);
                    TextView textTrailerSize = findViewById(R.id.txt_trailer_size);

                    if (dataVideo.size() > 2) {
                        textTrailerName.setText(dataVideo.get(2).getName());
                        textTrailerSite.setText(dataVideo.get(2).getSite());
                        textTrailerSize.setText(String.valueOf(dataVideo.get(2).getSize()));
                        String videoId = dataVideo.get(2).getKey();
                        youTubePlayer.cueVideo(videoId, 1);
                    }else if (dataVideo.size() > 1) {
                        textTrailerName.setText(dataVideo.get(1).getName());
                        textTrailerSite.setText(dataVideo.get(1).getSite());
                        textTrailerSize.setText(String.valueOf(dataVideo.get(1).getSize()));
                        String videoId = dataVideo.get(1).getKey();
                        youTubePlayer.cueVideo(videoId, 1);
                    }else{
                        textTrailerName.setText(dataVideo.get(0).getName());
                        textTrailerSite.setText(dataVideo.get(0).getSite());
                        textTrailerSize.setText(String.valueOf(dataVideo.get(0).getSize()));
                        String videoId = dataVideo.get(0).getKey();
                        youTubePlayer.cueVideo(videoId, 1);
                    }
                }
            }
        });
    }

    private void getRuntimeAndGenreFromServer(final int movie_id, final TextView txtRuntime) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(Interfaces.URL_TM_DB)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );
        ApiClient client = builder.client(httpClient.build()).build().create(ApiClient.class);
        Call<TVShowDetail> call = client.getRuntimeTVShow(movie_id, Interfaces.API_KEY);
        call.enqueue(new Callback<TVShowDetail>() {
            @Override
            public void onResponse(Call<TVShowDetail> call, Response<TVShowDetail> response) {
                TVShowDetail list = response.body();
                dataGenre = Objects.requireNonNull(list).getGenres();
                runtime = list.getNumber_of_episodes();
                saveDetailToDb(movie_id);

                setRuntimeAndGenreToView(txtRuntime);
            }

            @Override
            public void onFailure(Call<TVShowDetail> call, Throwable t) {
                Log.d("Failed", "onFailure: "+t);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setRuntimeAndGenreToView(TextView txtRuntime){
        txtRuntime.setText(runtime + "\nepisode");
        for (int x=0; x < dataGenre.size();x++){
            if (dataGenre.get(x).getName().equals("Family")){
                TextView family = findViewById(R.id.txt_detail_family);
                ImageView imgFamily = findViewById(R.id.img_family);
                imgFamily.setImageResource(R.drawable.family);
                family.setText(R.string.txt_title_family);
            }
            if (x == 0){
                Button first = findViewById(R.id.btn_detail_genre_1);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }else if(x == 1){
                Button first = findViewById(R.id.btn_detail_genre_2);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }else if(x == 2){
                Button first = findViewById(R.id.btn_detail_genre_3);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }else if(x == 3){
                Button first = findViewById(R.id.btn_detail_genre_4);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }else if(x == 4){
                Button first = findViewById(R.id.btn_detail_genre_5);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }else if(x == 5){
                Button first = findViewById(R.id.btn_detail_genre_6);
                first.setVisibility(View.VISIBLE);
                first.setText(dataGenre.get(x).getName());
            }
        }
    }

    public void saveFavoriteToDb(View view){
        TVShow tvShow = getIntent().getParcelableExtra(Interfaces.EXTRA_TV_SHOW);
        FavoriteTVShowTable data = mDb.favoriteTVShowDao().getOneFavoriteTVShow(tvShow.getId());
        if (data == null){
            FavoriteTVShowTable dbTV = new FavoriteTVShowTable();
            dbTV.id = tvShow.getId();
            dbTV.name = tvShow.getName();
            dbTV.overview = tvShow.getOverview();
            dbTV.vote_average = tvShow.getVote_average();
            dbTV.poster_path = tvShow.getPoster_path();
            dbTV.backdrop_path = tvShow.getBackdrop_path();
            dbTV.popularity = tvShow.getPopularity();
            dbTV.first_air_date = tvShow.getFirst_air_date();
            dbTV.vote_count = tvShow.getVote_count();
            mDb.favoriteTVShowDao().insertFavoriteTVShow(dbTV);

            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_remove_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDanger)));
        }else{
            mDb.favoriteTVShowDao().deleteOneFavoriteTVShow(tvShow.getId());
            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_add_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
    }

    private void checkFavoriteButton(){
        TVShow tvShow = getIntent().getParcelableExtra(Interfaces.EXTRA_TV_SHOW);
        FavoriteTVShowTable data = mDb.favoriteTVShowDao().getOneFavoriteTVShow(tvShow.getId());
        if (data == null){
            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_add_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }else{
            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_remove_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDanger)));
        }
    }

    private void saveDetailToDb(int movie_id){
        TVShowDetailTable dbTVShow = new TVShowDetailTable();
        dbTVShow.id = movie_id;
        dbTVShow.number_of_episodes = runtime;
        dbTVShow.genre = dataGenre;
        dbTVShow.video = dataVideo;

        mDb.tvShowDetailDao().insertTVShow(dbTVShow);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
