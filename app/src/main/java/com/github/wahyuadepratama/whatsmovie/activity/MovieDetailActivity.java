package com.github.wahyuadepratama.whatsmovie.activity;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.database.AppDatabase;
import com.github.wahyuadepratama.whatsmovie.database.favorite_movie.FavoriteMovieTable;
import com.github.wahyuadepratama.whatsmovie.database.movie.MovieDetailTable;
import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;
import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;
import com.github.wahyuadepratama.whatsmovie.model.movie.MovieDetail;
import com.github.wahyuadepratama.whatsmovie.model.video.Video;
import com.github.wahyuadepratama.whatsmovie.model.video.VideoList;
import com.github.wahyuadepratama.whatsmovie.utils.ApiClient;
import com.github.wahyuadepratama.whatsmovie.utils.GlideApp;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.github.wahyuadepratama.whatsmovie.widget.FavoriteMovieWidget;
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

public class MovieDetailActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private List<Video> dataVideo = new ArrayList<>();
    private List<Genre> dataGenre = new ArrayList<>();
    private int runtime;
    private LottieAnimationView like;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        like = findViewById(R.id.lav_actionBar);

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

        checkFavoriteButtonFromDb();

        if(getIntent() != null){
            final Movie movie = getIntent().getParcelableExtra(Interfaces.EXTRA_MOVIES);

            textTitle.setText(movie.getTitle());
            textDescription.setText(String.valueOf(movie.getOverview()));
            btnRelease.setText(movie.getRelease_date());
            txtReview.setText(movie.getVote_count() + "\nratings");
            txtPopularity.setText(movie.getPopularity() + "\nviewers");
            ratingBar.setRating((float)movie.getVote_average());
            txtRating.setText(String.valueOf(movie.getVote_average()));
            GlideApp.with(this)
                    .load(Interfaces.URL_IMG_TM_DB_ORIGINAL + movie.getBackdrop_path())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.loading_backdrop)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fallback(R.drawable.loading_backdrop)
                    .error(R.drawable.loading_backdrop)
                    .apply(new RequestOptions())
                    .into(imgBackdrop);
            GlideApp.with(this)
                    .load(Interfaces.URL_IMG_TM_DB + movie.getPoster_path())
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
                        .load(Interfaces.URL_IMG_TM_DB + movie.getBackdrop_path())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.loading_poster)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fallback(R.drawable.loading_poster)
                        .error(R.drawable.loading_poster)
                        .apply(new RequestOptions())
                        .into(bg);
            } else{
                GlideApp.with(this)
                        .load(Interfaces.URL_IMG_TM_DB + movie.getPoster_path())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.loading_poster)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fallback(R.drawable.loading_poster)
                        .error(R.drawable.loading_poster)
                        .apply(new RequestOptions())
                        .into(bg);
            }

            MovieDetailTable data = mDb.movieDetailDao().getDataMovieDetail(movie.getId());
            if (data == null){
                getRuntimeAndGenreFromServer(movie.getId(), txtRuntime);
                getVideoFromServer(movie.getId());
                setRuntimeAndGenreToView(txtRuntime);
                videoListener(youTubePlayerView);
            }else{
                if (data.genre.isEmpty()) {
                    getRuntimeAndGenreFromServer(movie.getId(), txtRuntime);
                }else{
                    dataGenre = data.genre;
                    runtime = data.runtime;
                    setRuntimeAndGenreToView(txtRuntime);
                }

                if(data.video.isEmpty()){
                    getVideoFromServer(movie.getId());
                }else{
                    dataVideo = data.video;
                }
                videoListener(youTubePlayerView);
            }
        }
    }

    private void getVideoFromServer(final int movie_id) {
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );
                ApiClient client = builder.client(httpClient.build()).build().create(ApiClient.class);
                Call<VideoList> call = client.getMovieVideo(movie_id, Interfaces.API_KEY);
                call.enqueue(new Callback<VideoList>() {
                    @Override
                    public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                        VideoList list = response.body();
                        dataVideo = Objects.requireNonNull(list).results;
                        saveDetailToDb(movie_id);
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
        Call<MovieDetail> call = client.getRuntimeMovie(movie_id, Interfaces.API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                MovieDetail list = response.body();
                dataGenre = Objects.requireNonNull(list).getGenres();
                runtime = list.getRuntime();
                saveDetailToDb(movie_id);

                setRuntimeAndGenreToView(txtRuntime);
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.d("Failed", "onFailure: "+t);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setRuntimeAndGenreToView(TextView txtRuntime){
        txtRuntime.setText(runtime + "\nminute");
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
        Movie movie = getIntent().getParcelableExtra(Interfaces.EXTRA_MOVIES);
        FavoriteMovieTable data = mDb.favoriteMovieDao().getOneFavoriteMovie(movie.getId());
        if (data == null){
            FavoriteMovieTable dbMovie = new FavoriteMovieTable();
            dbMovie.id = movie.getId();
            dbMovie.title = movie.getTitle();
            dbMovie.overview = movie.getOverview();
            dbMovie.vote_average = movie.getVote_average();
            dbMovie.poster_path = movie.getPoster_path();
            dbMovie.backdrop_path = movie.getBackdrop_path();
            dbMovie.popularity = movie.getPopularity();
            dbMovie.release_date = movie.getRelease_date();
            dbMovie.vote_count = movie.getVote_count();
            mDb.favoriteMovieDao().insertFavoriteMovie(dbMovie);

            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_remove_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDanger)));

            like.setVisibility(View.VISIBLE);
            like.playAnimation();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    like.setVisibility(View.GONE);
                }
            }, 800);

        }else{
            mDb.favoriteMovieDao().deleteOneFavoriteMovie(movie.getId());
            Button btnFavorite = findViewById(R.id.btn_favorite);
            btnFavorite.setText(getResources().getString(R.string.btn_add_favorite));
            btnFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }

        AppWidgetManager gm = AppWidgetManager.getInstance(this);
        int[] ids = gm.getAppWidgetIds(new ComponentName(this, FavoriteMovieWidget.class));
        gm.notifyAppWidgetViewDataChanged(ids ,R.id.stack_view);

    }

    private void checkFavoriteButtonFromDb(){
        Movie movie = getIntent().getParcelableExtra(Interfaces.EXTRA_MOVIES);
        FavoriteMovieTable data = mDb.favoriteMovieDao().getOneFavoriteMovie(movie.getId());
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
        MovieDetailTable dbMovie = new MovieDetailTable();
        dbMovie.id = movie_id;
        dbMovie.runtime = runtime;
        dbMovie.genre = dataGenre;
        dbMovie.video = dataVideo;

        mDb.movieDetailDao().insertMovie(dbMovie);

        AppWidgetManager gm = AppWidgetManager.getInstance(this);
        int[] ids = gm.getAppWidgetIds(new ComponentName(this, FavoriteMovieWidget.class));
        gm.notifyAppWidgetViewDataChanged(ids ,R.id.stack_view);
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
