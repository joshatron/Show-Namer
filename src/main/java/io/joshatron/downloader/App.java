package io.joshatron.downloader;

import io.joshatron.downloader.formatter.Formatter;
import io.joshatron.downloader.backend.*;
import io.joshatron.downloader.backend.EpisodeInfo;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class App {

    private static final String SERIES_OPTION = "series";
    private static final String SERIES_ID_OPTION = "seriesId";
    private static final String MOVIE_OPTION = "movie";
    private static final String MOVIE_ID_OPTION = "movieId";
    private static final String FORMAT_OPTION = "format";
    private static final String FORMAT_SERIES_PROPERTY = "format.episode";
    private static final String FORMAT_MOVIE_PROPERTY = "format.movie";
    private static final String BACKEND_OPTION = "backend";

    private static final String DEFAULT_EPISODE_FORMAT = "{seriesTitle.replace(' ', '_'}.{seriesYear}:S{seasonNumber}E{episodeNumber}:{episodeTitle.replace(' ', '_'}";
    private static final String DEFAULT_MOVIE_FORMAT = "{movieTitle.replace(' ', '_'}.{movieYear}";

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption(SERIES_OPTION) || cmd.hasOption(SERIES_ID_OPTION)) {
                renameEpisodes(properties, cmd);
            } else if(cmd.hasOption(MOVIE_OPTION) || cmd.hasOption(MOVIE_ID_OPTION)) {
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
        String format = DEFAULT_EPISODE_FORMAT;
        if(!properties.getProperty(FORMAT_SERIES_PROPERTY).isEmpty()) {
            format = properties.getProperty(FORMAT_SERIES_PROPERTY);
        }
        if(cmd.hasOption(FORMAT_OPTION)) {
            format = cmd.getOptionValue(FORMAT_OPTION);
        }
        Formatter formatter = new Formatter(format);

        String series = "";
        if(cmd.hasOption(SERIES_OPTION)) {
            series = cmd.getOptionValue(SERIES_OPTION);
        }
        String seriesId = "";
        if(cmd.hasOption(SERIES_ID_OPTION)) {
            seriesId = cmd.getOptionValue(SERIES_ID_OPTION);
        }

        SeriesInterface backend;
        if(cmd.hasOption(BACKEND_OPTION)) {
            backend = AppUtils.seriesInterfaceFromOption(cmd.getOptionValue(BACKEND_OPTION));
        } else {
            backend = AppUtils.seriesInterfaceFromOption("TVDB");
        }

        SeriesInfo info;
        if(seriesId.isEmpty()) {
            info = pickSeriesOption(backend.searchSeriesName(series));
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
        String format = DEFAULT_MOVIE_FORMAT;
        if(!properties.getProperty(FORMAT_MOVIE_PROPERTY).isEmpty()) {
            format = properties.getProperty(FORMAT_MOVIE_PROPERTY);
        }
        if(cmd.hasOption(FORMAT_OPTION)) {
            format = cmd.getOptionValue(FORMAT_OPTION);
        }
        Formatter formatter = new Formatter(format);

        String movie = "";
        if(cmd.hasOption(MOVIE_OPTION)) {
            movie = cmd.getOptionValue(MOVIE_OPTION);
        }
        String movieId = "";
        if(cmd.hasOption(MOVIE_ID_OPTION)) {
            movieId = cmd.getOptionValue(MOVIE_ID_OPTION);
        }

        MovieInteface backend;
        if(cmd.hasOption(BACKEND_OPTION)) {
            backend = AppUtils.movieInterfaceFromOption(cmd.getOptionValue(BACKEND_OPTION));
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

        Option seriesOption = new Option("s", SERIES_OPTION, true, "Series name");
        seriesOption.setRequired(false);
        options.addOption(seriesOption);

        Option seriesIdOption = new Option("sid", SERIES_ID_OPTION, true, "Series ID");
        seriesIdOption.setRequired(false);
        options.addOption(seriesIdOption);

        Option movieOption = new Option("m", MOVIE_OPTION, true, "Movie name");
        movieOption.setRequired(false);
        options.addOption(movieOption);

        Option movieIdOption = new Option("mid", MOVIE_ID_OPTION, true, "Movie ID");
        movieIdOption.setRequired(false);
        options.addOption(movieIdOption);

        Option formatOption = new Option("fo", FORMAT_OPTION, true, "File to parse");
        formatOption.setRequired(false);
        options.addOption(formatOption);

        Option metadataOption = new Option("b", BACKEND_OPTION, true, "Metadata backend to use (tvdb|tmdb)");
        metadataOption.setRequired(false);
        options.addOption(metadataOption);

        return options;
    }

    private static SeriesInfo pickSeriesOption(List<SeriesInfo> infos) {
        System.out.println("Multiple matches found, please choose a below option:");

        int i = 1;
        for(SeriesInfo info : infos) {
            System.out.println(i + ") " + info.getSeriesTitle() + ", " + info.getStartYear() + ", " + info.getSeriesDescription());
            i++;
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Choice: ");
            int option = Integer.parseInt(br.readLine().trim());

            return infos.get(option - 1);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new SeriesInfo();
    }
}
