package com.github.wahyuadepratama.favoritemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private double vote_average;
    private String poster_path;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private String release_date;

    public Movie(int id, String title, String overview, double vote_average, String poster_path, String backdrop_path, double popularity, String release_date, int vote_count) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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

    public double getPopularity() {
        return popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeString(this.release_date);
    }

    public Movie(Cursor data){
        this.id = data.getInt(data.getColumnIndex("id"));
        this.title = data.getString(data.getColumnIndex("title"));
        this.overview = data.getString(data.getColumnIndex("overview"));
        this.vote_average = data.getDouble(data.getColumnIndex("vote_average"));
        this.poster_path = data.getString(data.getColumnIndex("poster_path"));
        this.backdrop_path = data.getString(data.getColumnIndex("backdrop_path"));
        this.popularity = data.getDouble(data.getColumnIndex("popularity"));
        this.vote_count = data.getInt(data.getColumnIndex("vote_count"));
        this.release_date = data.getString(data.getColumnIndex("release_date"));
        data.moveToNext();
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_count  = in.readInt();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
