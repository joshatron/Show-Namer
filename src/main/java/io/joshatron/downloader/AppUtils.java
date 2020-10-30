package io.joshatron.downloader;

import io.joshatron.downloader.series.SeriesInfo;

public class AppUtils {
    public static String getPrettyNumber(int num) {
        if(num < 10) {
            return "0" + num;
        }
        else {
            return "" + num;
        }
    }

    public static String getExtension(String fileName) {
        if(fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }

        return "";
    }

    public static String formatEpisode(SeriesInfo series, int season, int episode, String episodeTitle, String format) {
        return format;
    }
}
