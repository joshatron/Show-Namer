package io.joshatron.downloader.movie;

import io.joshatron.downloader.App;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

public class TmdbInterfaceTest {

    @Test
    public void searchMovieName() {
        TmdbInterface tmdb = getInterface();

        tmdb.searchMovieName("Coco");
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