package io.joshatron.downloader.movie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieInfo {
    private String movieId;
    private String movieTitle;
    private String movieDescription;
    private int movieYear;

    public MovieInfo() {
        movieId = "";
        movieTitle = "";
        movieDescription = "";
        movieYear = 0;
    }
}
