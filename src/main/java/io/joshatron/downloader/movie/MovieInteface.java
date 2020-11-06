package io.joshatron.downloader.movie;

import java.util.List;

public interface MovieInteface {
    List<MovieInfo> searchMovieName(String movieName);
    MovieInfo getMovieInfo(String movieId);
}
