package com.github.wahyuadepratama.whatsmovie.model.movie;

import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;

import java.util.List;

public class MovieDetail {

    private List<Genre> genres;
    private int runtime;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
