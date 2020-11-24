package io.joshatron.downloader;

import io.joshatron.downloader.formatter.Formatter;
import io.joshatron.downloader.backend.*;
import io.joshatron.downloader.backend.EpisodeInfo;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("series") || cmd.hasOption("seriesId")) {
                renameEpisodes(properties, cmd);
            } else if(cmd.hasOption("movie") || cmd.hasOption("movieId")) {
                renameMovies(properties, cmd);
            } else {
                System.out.println("You need to enter either info about the series/movie.");
                new HelpFormatter().printHelp("Show Namer [OPTIONS] <FILE(S)>", options);
                System.exit(1);
            }
        } catch(Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            new HelpFormatter().printHelp("Show Namer [OPTIONS] <FILE(S)>", options);
        }
    }

    private static void renameEpisodes(Properties properties, CommandLine cmd) {
        String format = properties.getProperty("format.episode");
        if(cmd.hasOption("format")) {
            format = cmd.getOptionValue("format");
        }
        Formatter formatter = new Formatter(format);

        String series = "";
        if(cmd.hasOption("series")) {
            series = cmd.getOptionValue("series");
        }
        String seriesId = "";
        if(cmd.hasOption("seriesId")) {
            seriesId = cmd.getOptionValue("seriesId");
        }

        SeriesInterface backend;
        if(cmd.hasOption("backend")) {
            backend = AppUtils.seriesInterfaceFromOption(cmd.getOptionValue("backend"));
        } else {
            backend = AppUtils.seriesInterfaceFromOption("TVDB");
        }

        SeriesInfo info;
        if(seriesId.isEmpty()) {
            info = backend.searchSeriesName(series).get(0);
        } else {
            info = backend.getSeriesInfo(seriesId);
        }

        List<File> files = cmd.getArgList().stream().map(File::new).collect(Collectors.toList());

        for(File file : files) {
            int season = EpisodeAndSeasonPicker.getSeason(file.getName());
            int episode = EpisodeAndSeasonPicker.getEpisode(file.getName());

            if(season != -1 && episode != -1) {
                String episodeName = backend.getEpisodeTitle(info.getSeriesId(), season, episode);

                String newName = formatter.formatEpisode(new EpisodeInfo(info, season, episode, episodeName)) +
                        "." + AppUtils.getExtension(file.getName());
                File newFile = new File(file.getParentFile(), newName);

                System.out.println("Renamed " + file.getName() + " to " + newName);

                file.renameTo(newFile);
            } else {
                System.out.println("Failed to determine season and episode for " + file.getName());
            }
        }
    }

    private static void renameMovies(Properties properties, CommandLine cmd) {
        String format = properties.getProperty("format.movie");
        if(cmd.hasOption("format")) {
            format = cmd.getOptionValue("format");
        }
        Formatter formatter = new Formatter(format);

        String movie = "";
        if(cmd.hasOption("movie")) {
            movie = cmd.getOptionValue("movie");
        }
        String movieId = "";
        if(cmd.hasOption("movieId")) {
            movieId = cmd.getOptionValue("movieId");
        }

        MovieInteface backend;
        if(cmd.hasOption("backend")) {
            backend = AppUtils.movieInterfaceFromOption(cmd.getOptionValue("backend"));
        } else {
            backend = AppUtils.movieInterfaceFromOption("TMDB");
        }

        MovieInfo info;
        if(movieId.isEmpty()) {
            info = backend.searchMovieName(movie).get(0);
        } else {
            info = backend.getMovieInfo(movieId);
        }

        List<File> files = cmd.getArgList().stream().map(File::new).collect(Collectors.toList());

        for(File file : files) {
            String newName = formatter.formatMovie(info) + "." + AppUtils.getExtension(file.getName());
            File newFile = new File(file.getParentFile(), newName);

            System.out.println("Renamed " + file.getName() + " to " + newName);

            file.renameTo(newFile);
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        Option seriesOption = new Option("s", "series", true, "Series name");
        seriesOption.setRequired(false);
        options.addOption(seriesOption);

        Option seriesIdOption = new Option("sid", "seriesId", true, "Series ID");
        seriesIdOption.setRequired(false);
        options.addOption(seriesIdOption);

        Option movieOption = new Option("m", "movie", true, "Movie name");
        movieOption.setRequired(false);
        options.addOption(movieOption);

        Option movieIdOption = new Option("mid", "movieId", true, "Movie ID");
        movieIdOption.setRequired(false);
        options.addOption(movieIdOption);

        Option formatOption = new Option("fo", "format", true, "File to parse");
        formatOption.setRequired(false);
        options.addOption(formatOption);

        Option metadataOption = new Option("b", "backend", true, "Metadata backend to use (tvdb|tmdb)");
        metadataOption.setRequired(false);
        options.addOption(metadataOption);

        return options;
    }
}
