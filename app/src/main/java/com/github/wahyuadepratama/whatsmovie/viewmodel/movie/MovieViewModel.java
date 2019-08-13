package com.github.wahyuadepratama.whatsmovie.viewmodel.movie;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.wahyuadepratama.whatsmovie.model.movie.Movie;
import com.github.wahyuadepratama.whatsmovie.model.movie.MovieList;
import com.github.wahyuadepratama.whatsmovie.utils.ApiClient;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> listMoviesLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Movie>> getListMoviesLive() {
        return listMoviesLive;
    }

    MovieViewModel(String status) {
        getDataDiscover(status);
    }

    public void getDataSearch(final String query){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );

                Retrofit retrofit = builder.client(httpClient.build()).build();
                ApiClient client = retrofit.create(ApiClient.class);
                Call<MovieList> call;

                call = client.getSearchMovies(Interfaces.API_KEY, checkLanguage(), checkRegion(), query);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        MovieList list = response.body();
                        List<Movie> listMovie = Objects.requireNonNull(list).results;
                        listMoviesLive.postValue((ArrayList<Movie>) listMovie);
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: --------------------------------------"+t.getMessage());
                    }
                });
            }
        }).start();
    }

    public void getDataUpcoming(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );

                Retrofit retrofit = builder.client(httpClient.build()).build();
                ApiClient client = retrofit.create(ApiClient.class);
                Call<MovieList> call;

                call = client.getUpcomingMovies(Interfaces.API_KEY, checkLanguage(), checkRegion());

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        MovieList list = response.body();
                        List<Movie> listMovie = Objects.requireNonNull(list).results;
                        listMoviesLive.postValue((ArrayList<Movie>) listMovie);
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: --------------------------------------"+t.getMessage());
                    }
                });
            }
        }).start();
    }

    public void getDataNowPlaying(){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );

                Retrofit retrofit = builder.client(httpClient.build()).build();
                ApiClient client = retrofit.create(ApiClient.class);
                Call<MovieList> call;

                call = client.getNowPlayingMovies(Interfaces.API_KEY, checkLanguage(), checkRegion());

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        MovieList list = response.body();
                        List<Movie> listMovie = Objects.requireNonNull(list).results;
                        listMoviesLive.postValue((ArrayList<Movie>) listMovie);
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: --------------------------------------"+t.getMessage());
                    }
                });
            }
        }).start();
    }

    public void getDataDiscover(final String status){
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(Interfaces.URL_TM_DB)
                                .addConverterFactory(
                                        GsonConverterFactory.create()
                                );

                Retrofit retrofit = builder.client(httpClient.build()).build();
                ApiClient client = retrofit.create(ApiClient.class);
                Call<MovieList> call;

                call = client.getPopularMovies(Interfaces.API_KEY, checkLanguage(), checkRegion(), status);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        MovieList list = response.body();
                        List<Movie> listMovie = Objects.requireNonNull(list).results;
                        listMoviesLive.postValue((ArrayList<Movie>) listMovie);
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: --------------------------------------"+t.getMessage());
                    }
                });
            }
        }).start();
    }

    private String checkLanguage(){
        if(Locale.getDefault().getLanguage().equals("en"))
            return "en-US";
        else if(Locale.getDefault().getLanguage().equals("in"))
            return "id";
        else
            return "en-US";
    }

    private String checkRegion(){
        if(Locale.getDefault().getLanguage().equals("en"))
            return "us";
        else if(Locale.getDefault().getLanguage().equals("in"))
            return "id";
        else
            return "us";
    }
}
