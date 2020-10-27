package io.joshatron.downloader;

import io.joshatron.downloader.metadata.TvdbInterface;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        Options options = new Options();

        Option seriesOption = new Option("s", "series", true, "Series name");
        seriesOption.setRequired(true);
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

            String series = cmd.getOptionValue("series");
            File file = new File(cmd.getOptionValue("file"));
            String seriesId = "";
            if(cmd.hasOption("seriesId")) {
                seriesId = cmd.getOptionValue("seriesId");
            } else {
                seriesId = tvdbInterface.getSeriesId(series.replace(" ", "_"));
            }
            int season = EpisodeAndSeasonPicker.getSeason(file.getName());
            int episode = EpisodeAndSeasonPicker.getEpisode(file.getName());

            String episodeName = tvdbInterface.getEpisodeName(seriesId, season, episode);
            String newName = tvdbInterface.generateSeriesName(seriesId) + ":"
                    + "S" + AppUtils.getPrettyNumber(season)
                    + "E" + AppUtils.getPrettyNumber(episode) + ":" +
                    episodeName.replace(" ", "_")
                    + "." + AppUtils.getExtension(file.getName());
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
