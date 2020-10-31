package io.joshatron.downloader;

import io.joshatron.downloader.formatter.EpisodeFormatter;
import io.joshatron.downloader.series.SeriesInfo;
import io.joshatron.downloader.series.TvdbInterface;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        Options options = new Options();

        Option seriesOption = new Option("s", "series", true, "Series name");
        seriesOption.setRequired(false);
        options.addOption(seriesOption);

        Option seriesIdOption = new Option("id", "seriesId", true, "TVDB series ID");
        seriesIdOption.setRequired(false);
        options.addOption(seriesIdOption);

        Option fileOption = new Option("f", "file", true, "File to parse");
        fileOption.setRequired(true);
        options.addOption(fileOption);

        CommandLineParser parser = new DefaultParser();

        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            TvdbInterface tvdbInterface = new TvdbInterface(properties.getProperty("tvdb-api-key"));

            CommandLine cmd = parser.parse(options, args);

            String series = "";
            if(cmd.hasOption("series")) {
                series = cmd.getOptionValue("series");
            }
            File file = new File(cmd.getOptionValue("file"));
            String seriesId = "";
            if(cmd.hasOption("seriesId")) {
                seriesId = cmd.getOptionValue("seriesId");
            }
            int season = EpisodeAndSeasonPicker.getSeason(file.getName());
            int episode = EpisodeAndSeasonPicker.getEpisode(file.getName());

            if(series.isEmpty() && seriesId.isEmpty()) {
                System.out.println("You need to enter either the series or the series ID.");
                System.exit(1);
            }

            SeriesInfo info;
            if(seriesId.isEmpty()) {
                info = tvdbInterface.searchSeriesName(series).get(0);
            } else {
                info = tvdbInterface.getSeriesInfo(seriesId);
            }
            String episodeName = tvdbInterface.getEpisodeTitle(info.getSeriesId(), season, episode);

            String format = "{seriesTitle}.{seriesYear}:S{season}E{episode}:{episodeTitle}";
            EpisodeFormatter formatter = new EpisodeFormatter(format);

            String newName = formatter.formatEpisode(info, season, episode, episodeName).replace(" ", "_") +
                    "." + AppUtils.getExtension(file.getName());
            File newFile = new File(file.getParentFile(), newName);

            System.out.println("Renamed " + file.getName() + " to " + newName);

            file.renameTo(newFile);
        } catch(ParseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("Show Namer", options);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
