package io.joshatron.downloader.movie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieInfo {
    String movieId;
    String movieTitle;
    int movieYear;

    public MovieInfo() {
        movieId = "";
        movieTitle = "";
        movieYear = 0;
    }
}
