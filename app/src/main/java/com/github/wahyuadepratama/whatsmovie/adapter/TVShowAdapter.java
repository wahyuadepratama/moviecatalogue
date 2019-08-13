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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private Context context;
    private ArrayList<TVShow> listTVShow;
    private OnTVShowItemClicked clickHandler;
    private String sort_by;

    public void setClickHandler(OnTVShowItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
    }

    public TVShowAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<TVShow> getListTVShow() {
        return listTVShow;
    }

    public void setListTVShow(ArrayList<TVShow> listTVShow) {
        ArrayList<TVShow> temp = new ArrayList<>();
        for (int i=listTVShow.size()-1; i >= 0; i--){
            if (listTVShow.get(i).getOverview().equals(""))
                listTVShow.get(i).setOverview(context.getResources().getString(R.string.not_supported_language_tv));

            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
            String startDateStr = listTVShow.get(i).getFirst_air_date();
            try {
                Date date = inputFormat.parse(startDateStr);
                String startDateStrNewFormat = outputFormat.format(date);
                listTVShow.get(i).setFirst_air_date(startDateStrNewFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            temp.add(listTVShow.get(i));
        }

        Collections.reverse(temp);
        this.listTVShow = temp;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVShowAdapter.TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv, viewGroup, false);
        return new TVShowViewHolder(itemRow);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.TVShowViewHolder tvShowViewHolder, int i) {
        tvShowViewHolder.tvTitle.setText(getListTVShow().get(i).getName());
        tvShowViewHolder.tvOverview.setText(getListTVShow().get(i).getOverview());
        tvShowViewHolder.ratingBar.setRating((float)getListTVShow().get(i).getVote_average());

        Glide.with(context)
                .load(Interfaces.URL_IMG_TM_DB + getListTVShow().get(i).getBackdrop_path())
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_backdrop)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fallback(R.drawable.loading_backdrop)
                .error(R.drawable.loading_backdrop)
                .apply(new RequestOptions())
                .into(tvShowViewHolder.imgPoster);

        checkOrientationChange(tvShowViewHolder, i);
    }

    @Override
    public int getItemCount() {
        return getListTVShow().size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvOverview, tvRelease;
        AppCompatRatingBar ratingBar;
        ImageView imgPoster;
        Button btnRating, notificationInfo;
        View barrier;

        private TVShowViewHolder(@NonNull View itemView) {
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
                    TVShow m = listTVShow.get(position);
                    clickHandler.tvShowItemClicked(m);
                }
            });
        }
    }

    public interface OnTVShowItemClicked {
        void tvShowItemClicked(TVShow m);
    }

    private void checkOrientationChange(TVShowViewHolder tvShowViewHolder, int i){
        int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT && i == 0){
            tvShowViewHolder.notificationInfo.setVisibility(View.VISIBLE);
            tvShowViewHolder.barrier.setVisibility(View.VISIBLE);
            tvShowViewHolder.notificationInfo.setText(sort_by);
        }else{
            tvShowViewHolder.barrier.setVisibility(View.GONE);
            tvShowViewHolder.notificationInfo.setVisibility(View.GONE);
            if(orientation == Configuration.ORIENTATION_LANDSCAPE && i == 0 || orientation == Configuration.ORIENTATION_LANDSCAPE && i == 1){
                tvShowViewHolder.barrier.setVisibility(View.VISIBLE);
            }
        }
    }
}
