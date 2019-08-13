package com.github.wahyuadepratama.whatsmovie.model.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

public class TVShow implements Parcelable {

    private int id;
    private String name;
    private String overview;
    private double vote_average;
    private String poster_path;
    private String backdrop_path;
    private String first_air_date;
    private int vote_count;
    private double popularity;

    public TVShow(int id, String name, String overview, double vote_average, String poster_path, String backdrop_path, String first_air_date, int vote_count, double popularity) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.first_air_date = first_air_date;
        this.vote_count = vote_count;
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public double getPopularity() {
        return popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.first_air_date);
        dest.writeInt(this.vote_count);
        dest.writeDouble(this.popularity);
        dest.writeString(this.first_air_date);
    }

    private TVShow(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.first_air_date = in.readString();
        this.vote_count = in.readInt();
        this.popularity = in.readDouble();
        this.first_air_date = in.readString();
    }

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
