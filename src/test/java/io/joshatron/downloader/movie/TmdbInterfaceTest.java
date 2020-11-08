package io.joshatron.downloader.movie;

import io.joshatron.downloader.App;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TmdbInterfaceTest {

    @Test
    public void searchMovieName() {
        TmdbInterface tmdb = getInterface();

        List<MovieInfo> movies = tmdb.searchMovieName("Coco");
        Assert.assertFalse(movies.isEmpty());
        Assert.assertEquals(movies.get(0).getMovieId(), "354912");
        Assert.assertEquals(movies.get(0).getMovieTitle(), "Coco");
        Assert.assertEquals(movies.get(0).getMovieYear(), 2017);
        Assert.assertFalse(movies.get(0).getMovieDescription().isEmpty());
    }

    private TmdbInterface getInterface() {
        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            return new TmdbInterface(properties.getProperty("tmdb.api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}