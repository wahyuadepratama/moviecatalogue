package com.github.wahyuadepratama.whatsmovie.utils;

import com.github.wahyuadepratama.whatsmovie.model.movie.MovieDetail;
import com.github.wahyuadepratama.whatsmovie.model.movie.MovieList;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShowDetail;
import com.github.wahyuadepratama.whatsmovie.model.tvshow.TVShowList;
import com.github.wahyuadepratama.whatsmovie.model.video.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {
    // _____________________________________Movie
    @GET("3/discover/movie")
    Call<MovieList> getPopularMovies(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("region") String region,
                                     @Query("sort_by") String popularity);

    @GET("3/movie/{movie_id}/videos")
    Call<VideoList> getMovieVideo(@Path("movie_id") int id,
                                  @Query("api_key") String api_key);

    @GET("/3/movie/upcoming")
    Call<MovieList> getUpcomingMovies(@Query("api_key") String api_key,
                                      @Query("language") String language,
                                      @Query("region") String region);

    @GET("/3/movie/now_playing")
    Call<MovieList> getNowPlayingMovies(@Query("api_key") String api_key,
                                        @Query("language") String language,
                                        @Query("region") String region);

    @GET("3/search/movie")
    Call<MovieList> getSearchMovies(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("region") String region,
                                     @Query("query") String query);

    @GET("3/movie/{movie_id}")
    Call<MovieDetail> getRuntimeMovie(@Path("movie_id") int id,
                                      @Query("api_key") String api_key);

    // ___________________________________TV Show
    @GET("3/discover/tv")
    Call<TVShowList> getPopularTVShow(@Query("api_key") String api_key,
                                      @Query("language") String language,
                                      @Query("region") String region,
                                      @Query("sort_by") String popularity);

    @GET("3/tv/{tv_id}/videos")
    Call<VideoList> getTVShowVideo(@Path("tv_id") int id,
                                   @Query("api_key") String api_key);

    @GET("3/search/tv")
    Call<TVShowList> getSearchTVShow(@Query("api_key") String api_key,
                                    @Query("language") String language,
                                    @Query("region") String region,
                                    @Query("query") String query);

    @GET("3/discover/movie")
    Call<MovieList> getReleaseToday(@Query("api_key") String api_key,
                                     @Query("primary_release_date.gte") String date);

    @GET("3/tv/{tv_id}")
    Call<TVShowDetail> getRuntimeTVShow(@Path("tv_id") int id,
                                       @Query("api_key") String api_key);
}
