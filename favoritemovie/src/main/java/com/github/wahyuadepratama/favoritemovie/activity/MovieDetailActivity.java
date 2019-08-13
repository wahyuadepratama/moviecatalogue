package com.github.wahyuadepratama.favoritemovie.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.favoritemovie.R;
import com.github.wahyuadepratama.favoritemovie.model.Movie;
import com.github.wahyuadepratama.favoritemovie.utils.Static;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Movie movie = getIntent().getParcelableExtra(Static.EXTRA_MOVIES);

        ImageView poster = findViewById(R.id.img_detail_poster);
        ImageView bg_toolbar = findViewById(R.id.bg_toolbar);
        AppCompatRatingBar ratingBar = findViewById(R.id.rt_bar);
        TextView title = findViewById(R.id.txt_detail_title);
        TextView release = findViewById(R.id.txt_detail_release);
        TextView overview = findViewById(R.id.txt_detail_overview);
        TextView rating = findViewById(R.id.txt_detail_rating);
        TextView review = findViewById(R.id.txt_detail_review);

        title.setText(movie.getTitle());
        release.setText(movie.getRelease_date());
        ratingBar.setRating((float)movie.getVote_average());
        overview.setText(movie.getOverview());
        rating.setText("Rating " + movie.getVote_average());
        review.setText(movie.getVote_count() + " Reviewer");
        Glide.with(this)
                .load(Static.URL_IMG_TM_DB + movie.getPoster_path())
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_poster)
                .error(R.drawable.loading_poster)
                .apply(new RequestOptions())
                .into(poster);
        Glide.with(this)
                .load(Static.URL_IMG_TM_DB + movie.getBackdrop_path())
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_backdrop)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_backdrop)
                .error(R.drawable.loading_backdrop)
                .apply(new RequestOptions())
                .into(bg_toolbar);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(movie.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
