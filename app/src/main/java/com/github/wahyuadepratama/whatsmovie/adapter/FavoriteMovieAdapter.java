package com.github.wahyuadepratama.whatsmovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovie;
    private FavoriteMovieAdapter.OnMovieItemClicked clickHandler;

    public void setClickHandler(OnMovieItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> list) {
        listMovie = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_movie, parent, false);
        return new FavoriteMovieViewHolder(itemRow);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieViewHolder holder, int i) {

        if (getItemCount() == 0)
            holder.txtInfoFavorite.setText(R.string.info_favorite_movie);

        holder.tvTitle.setText(getListMovie().get(i).getTitle());

        Glide.with(context)
                .load(Interfaces.URL_IMG_TM_DB + getListMovie().get(i).getPoster_path())
                .placeholder(R.drawable.loading_poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_poster)
                .error(R.drawable.loading_poster)
                .apply(new RequestOptions())
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public interface OnMovieItemClicked {
        void movieItemClicked(Movie m);
    }

    class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, txtInfoFavorite;
        ImageView imgPoster;
        FavoriteMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_detail_title);
            imgPoster = itemView.findViewById(R.id.img_detail_poster);
            txtInfoFavorite = itemView.findViewById(R.id.txt_info_favorite);

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
}
