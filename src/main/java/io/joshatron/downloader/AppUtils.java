package io.joshatron.downloader;

import io.joshatron.downloader.metadata.MovieInteface;
import io.joshatron.downloader.metadata.SeriesInterface;
import io.joshatron.downloader.metadata.TmdbInterface;
import io.joshatron.downloader.metadata.TvdbInterface;

import java.io.IOException;
import java.util.Properties;

public class AppUtils {
    public static String getExtension(String fileName) {
        if(fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }

        return "";
    }

    public static SeriesInterface seriesInterfaceFromOption(String option) {
        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

            if (option.equalsIgnoreCase("TVDB")) {
                return new TvdbInterface(properties.getProperty("tvdb.api-key"));
            } else if (option.equalsIgnoreCase("TMDB")) {
                return new TmdbInterface(properties.getProperty("tmdb.api-key"));
            } else {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static MovieInteface movieInterfaceFromOption(String option) {
        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

            if (option.equalsIgnoreCase("TMDB")) {
                return new TmdbInterface(properties.getProperty("tmdb.api-key"));
            } else {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
