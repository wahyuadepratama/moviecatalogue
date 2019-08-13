package com.github.wahyuadepratama.whatsmovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;
import com.github.wahyuadepratama.whatsmovie.utils.GlideApp;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovie;
    private OnMovieItemClicked clickHandler;
    private String sort_by;

    public void setClickHandler(OnMovieItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        ArrayList<Movie> temp = new ArrayList<>();
        for (int i=listMovie.size()-1; i >= 0; i--){
            if (listMovie.get(i).getOverview().equals(""))
               listMovie.get(i).setOverview(context.getResources().getString(R.string.not_supported_language_movie));

            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
            String startDateStr = listMovie.get(i).getRelease_date();
            try {
                Date date = inputFormat.parse(startDateStr);
                String startDateStrNewFormat = outputFormat.format(date);
                listMovie.get(i).setRelease_date(startDateStrNewFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            temp.add(listMovie.get(i));
        }

        Collections.reverse(temp);
        this.listMovie = temp;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieViewHolder(itemRow);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.tvTitle.setText(getListMovie().get(i).getTitle());
        movieViewHolder.ratingBar.setRating((float)getListMovie().get(i).getVote_average());
        movieViewHolder.tvOverview.setText(getListMovie().get(i).getOverview());

        GlideApp.with(context)
                .load(Interfaces.URL_IMG_TM_DB + getListMovie().get(i).getPoster_path())
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_poster)
                .error(R.drawable.loading_poster)
                .apply(new RequestOptions())
                .into(movieViewHolder.imgPoster);

        checkOrientationChange(movieViewHolder, i);
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvOverview;
        AppCompatRatingBar ratingBar;
        ImageView imgPoster;
        Button notificationInfo;
        View barrier;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_detail_title);
            tvOverview = itemView.findViewById(R.id.txt_detail_description);
            imgPoster = itemView.findViewById(R.id.img_detail_poster);
            ratingBar = itemView.findViewById(R.id.rt_bar);
            notificationInfo = itemView.findViewById(R.id.top_info);
            barrier = itemView.findViewById(R.id.top_barrier);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Movie m = listMovie.get(position);
                    clickHandler.movieItemClicked(m);
                }
            });
        }
    }

    public interface OnMovieItemClicked{
        void movieItemClicked(Movie m);
    }

    private void checkOrientationChange(MovieAdapter.MovieViewHolder movieViewHolder, int i){
        int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT && i == 0){
            movieViewHolder.notificationInfo.setVisibility(View.VISIBLE);
            movieViewHolder.barrier.setVisibility(View.VISIBLE);
            movieViewHolder.notificationInfo.setText(sort_by);
        }else{
            movieViewHolder.barrier.setVisibility(View.GONE);
            movieViewHolder.notificationInfo.setVisibility(View.GONE);
            if(orientation == Configuration.ORIENTATION_LANDSCAPE && i == 0 || orientation == Configuration.ORIENTATION_LANDSCAPE && i == 1){
                movieViewHolder.barrier.setVisibility(View.VISIBLE);
            }
        }
    }
}
