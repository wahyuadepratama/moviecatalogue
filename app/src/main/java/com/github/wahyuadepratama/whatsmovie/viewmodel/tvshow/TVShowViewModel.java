package com.github.wahyuadepratama.whatsmovie.viewmodel.tvshow;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShow;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShowList;
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

public class TVShowViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TVShow>> listTVShow = new MutableLiveData<>();
    public MutableLiveData<ArrayList<TVShow>> getListTVShow() {
        return listTVShow;
    }

    TVShowViewModel(String status) {
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
                Call<TVShowList> call;

                call = client.getSearchTVShow(Interfaces.API_KEY, checkLanguage(), checkRegion(), query);

                call.enqueue(new Callback<TVShowList>() {
                    @Override
                    public void onResponse(Call<TVShowList> call, Response<TVShowList> response) {
                        TVShowList list = response.body();
                        List<TVShow> listTv = Objects.requireNonNull(list).results;
                        listTVShow.postValue((ArrayList<TVShow>) listTv);
                    }

                    @Override
                    public void onFailure(Call<TVShowList> call, Throwable t) {
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
                Call<TVShowList> call;

                call = client.getPopularTVShow(Interfaces.API_KEY, checkLanguage(), checkRegion(), status);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<TVShowList>() {
                    @Override
                    public void onResponse(Call<TVShowList> call, Response<TVShowList> response) {
                        TVShowList list = response.body();
                        List<TVShow> listTv = Objects.requireNonNull(list).results;
                        listTVShow.postValue((ArrayList<TVShow>) listTv);
                    }

                    @Override
                    public void onFailure(Call<TVShowList> call, Throwable t) {
                        Log.d(TAG, "onFailure: --------------------------------------"+t.getMessage());
                    }
                });
            }
        }).start();
    }

    private String checkLanguage(){
        if(Locale.getDefault().getLanguage().equals("en")){
            return "en-US";
        }else if(Locale.getDefault().getLanguage().equals("in")){
            return "id";
        }else{
            return "en-US";
        }
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
