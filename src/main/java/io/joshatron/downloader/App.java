package io.joshatron.downloader;

import org.apache.commons.cli.*;

import java.io.File;

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
            int season = getSeason(cmd.getOptionValue("file"));
            int episode = getEpisode(cmd.getOptionValue("file"));

            TvdbInterface tvdbInterface = new TvdbInterface();
            String episodeName = tvdbInterface.getEpisode(seriesId, season, episode);
            String newName = series + ":" + "S" + getPrettyNumber(season) + "E" + getPrettyNumber(episode)
                    + ":" + episodeName.replace(" ", "_") + ".mkv";
            File newFile = new File(file.getParentFile(), newName);

            file.renameTo(newFile);
        } catch(ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("Show Namer", options);
        }
    }

    private static int getSeason(String fileName) {
        String[] ss = fileName.split("S");
        for(String s : ss) {
            int nums = numsStarting(s);
            if(nums == 1 || nums == 2) {
                return Integer.parseInt(s.substring(0, nums));
            }
        }

        return -1;
    }

    private static int getEpisode(String fileName) {
        String[] es = fileName.split("E");
        for(String e : es) {
            int nums = numsStarting(e);
            if(nums == 1 || nums == 2) {
                return Integer.parseInt(e.substring(0, nums));
            }
        }
        return 1;
    }

    private static int numsStarting(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return i;
            }
        }

        return str.length();
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
