package io.joshatron.downloader.formatter;

import io.joshatron.downloader.series.SeriesInfo;

import java.util.ArrayList;
import java.util.List;

public class EpisodeFormatter {
    private String format;

    public EpisodeFormatter(String format) {
        this.format = format;
    }

    public String formatEpisode(SeriesInfo series, int season, int episode, String episodeTitle) {
        return format
                .replace("{seriesTitle}", series.getSeriesTitle())
                .replace("{seriesYear}", Integer.toString(series.getStartYear()))
                .replace("{episodeTitle}", episodeTitle)
                .replace("{season}", getPrettyNumber(season))
                .replace("{episode}", getPrettyNumber(episode));
    }

    private List<String> toParts(String format) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        for(char c : format.toCharArray()) {
            if(c == '{' || c == '}') {
                parts.add(currentPart.toString());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }

        return parts;
    }

    private String getPrettyNumber(int num) {
        if(num < 10) {
            return "0" + num;
        }
        else {
            return "" + num;
        }
    }
}
