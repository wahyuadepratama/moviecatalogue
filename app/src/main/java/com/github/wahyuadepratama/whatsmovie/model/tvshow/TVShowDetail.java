package com.github.wahyuadepratama.whatsmovie.model.tvshow;

import com.github.wahyuadepratama.whatsmovie.model.genre.Genre;

import java.util.List;

public class TVShowDetail {

    private List<Genre> genres;
    private int number_of_episodes;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }
}
