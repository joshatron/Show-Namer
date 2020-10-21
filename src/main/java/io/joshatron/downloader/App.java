package io.joshatron.downloader;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
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
        seriesIdOption.setRequired(true);
        options.addOption(seriesIdOption);

        Option fileOption = new Option("f", "file", true, "File to parse");
        fileOption.setRequired(true);
        options.addOption(fileOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            String series = cmd.getOptionValue("series");
            String seriesId = cmd.getOptionValue("seriesId");
            File file = new File(cmd.getOptionValue("file"));
            int season = EpisodeAndSeasonPicker.getSeason(cmd.getOptionValue("file"));
            int episode = EpisodeAndSeasonPicker.getEpisode(cmd.getOptionValue("file"));

            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            TvdbInterface tvdbInterface = new TvdbInterface(properties.getProperty("tvdb-api-key"));
            String episodeName = tvdbInterface.getEpisode(seriesId, season, episode);
            String newName = series + ":" + "S" + getPrettyNumber(season) + "E" + getPrettyNumber(episode)
                    + ":" + episodeName.replace(" ", "_") + ".mkv";
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

    private static String getPrettyNumber(int num) {
        if(num < 10) {
            return "0" + num;
        }
        else {
            return "" + num;
        }
    }
}
