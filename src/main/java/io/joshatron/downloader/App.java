package io.joshatron.downloader;

import io.joshatron.downloader.formatter.EpisodeFormatter;
import io.joshatron.downloader.formatter.EpisodeMetadata;
import io.joshatron.downloader.metadata.SeriesInfo;
import io.joshatron.downloader.metadata.TvdbInterface;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        Options options = new Options();

        Option seriesOption = new Option("s", "series", true, "Series name");
        seriesOption.setRequired(false);
        options.addOption(seriesOption);

        Option seriesIdOption = new Option("id", "seriesId", true, "TVDB series ID");
        seriesIdOption.setRequired(false);
        options.addOption(seriesIdOption);

        Option formatOption = new Option("fo", "format", true, "File to parse");
        formatOption.setRequired(false);
        options.addOption(formatOption);

        CommandLineParser parser = new DefaultParser();

        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            TvdbInterface tvdbInterface = new TvdbInterface(properties.getProperty("tvdb.api-key"));

            CommandLine cmd = parser.parse(options, args);

            String format = properties.getProperty("format.episode");
            if(cmd.hasOption("format")) {
                format = cmd.getOptionValue("format");
            }
            EpisodeFormatter formatter = new EpisodeFormatter(format);

            String series = "";
            if(cmd.hasOption("series")) {
                series = cmd.getOptionValue("series");
            }
            String seriesId = "";
            if (cmd.hasOption("seriesId")) {
                seriesId = cmd.getOptionValue("seriesId");
            }

            if (series.isEmpty() && seriesId.isEmpty()) {
                System.out.println("You need to enter either the series or the series ID.");
                System.exit(1);
            }

            SeriesInfo info;
            if (seriesId.isEmpty()) {
                info = tvdbInterface.searchSeriesName(series).get(0);
            } else {
                info = tvdbInterface.getSeriesInfo(seriesId);
            }

            List<File> files = cmd.getArgList().stream().map(File::new).collect(Collectors.toList());

            for(File file : files) {
                int season = EpisodeAndSeasonPicker.getSeason(file.getName());
                int episode = EpisodeAndSeasonPicker.getEpisode(file.getName());

                if(season != -1 && episode != -1) {
                    String episodeName = tvdbInterface.getEpisodeTitle(info.getSeriesId(), season, episode);

                    String newName = formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeName)) +
                            "." + AppUtils.getExtension(file.getName());
                    File newFile = new File(file.getParentFile(), newName);

                    System.out.println("Renamed " + file.getName() + " to " + newName);

                    file.renameTo(newFile);
                } else {
                    System.out.println("Failed to determine season and episode for " + file.getName());
                }
            }
        } catch(ParseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("Show Namer [OPTIONS] <FILE(S)>", options);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
