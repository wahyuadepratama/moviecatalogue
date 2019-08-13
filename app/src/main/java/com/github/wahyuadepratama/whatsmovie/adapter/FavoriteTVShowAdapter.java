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
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;

import java.util.ArrayList;

public class FavoriteTVShowAdapter extends RecyclerView.Adapter<FavoriteTVShowAdapter.FavoriteTVShowViewHolder> {

    private Context context;
    private ArrayList<TVShow> listTVShow;
    private OnTVShowItemClicked clickHandler;

    public FavoriteTVShowAdapter(Context context) {
        this.context = context;
    }

    public void setListTVShow(ArrayList<TVShow> listTVShow) {
        this.listTVShow = listTVShow;
        notifyDataSetChanged();
    }

    public void setClickHandler(OnTVShowItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

    private ArrayList<TVShow> getListTVShow() {
        return listTVShow;
    }

    @NonNull
    @Override
    public FavoriteTVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_tvshow, parent, false);
        return new FavoriteTVShowAdapter.FavoriteTVShowViewHolder(itemRow);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteTVShowViewHolder holder, int i) {

        holder.tvTitle.setText(getListTVShow().get(i).getName());

        Glide.with(context)
                .load(Interfaces.URL_IMG_TM_DB + getListTVShow().get(i).getPoster_path())
                .placeholder(R.drawable.loading_poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_poster)
                .error(R.drawable.loading_poster)
                .apply(new RequestOptions())
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return getListTVShow().size();
    }

    public interface OnTVShowItemClicked {
        void tvShowItemClicked(TVShow m);
    }

    class FavoriteTVShowViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgPoster;
        FavoriteTVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_detail_title);
            imgPoster = itemView.findViewById(R.id.img_detail_poster);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    TVShow m = listTVShow.get(position);
                    clickHandler.tvShowItemClicked(m);
                }
            });
        }
    }
}
