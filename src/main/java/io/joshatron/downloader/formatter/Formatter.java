package io.joshatron.downloader.formatter;

import io.joshatron.downloader.metadata.EpisodeInfo;
import io.joshatron.downloader.metadata.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class Formatter {
    private List<FormatPart> formatParts;

    public Formatter(String format) {
        this.formatParts = separate(format);
    }

    private List<FormatPart> separate(String format) {
        List<FormatPart> parts = new ArrayList<>();

        StringBuilder currentPart = new StringBuilder();

        for(char c : format.toCharArray()) {
            if(c == '{') {
                if(currentPart.length() != 0) {
                    parts.add(new FormatPart(false, currentPart.toString()));
                    currentPart = new StringBuilder();
                }
            } else if (c == '}') {
                parts.add(new FormatPart(true, currentPart.toString()));
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }

        if(currentPart.length() != 0) {
            parts.add(new FormatPart(false, currentPart.toString()));
        }

        return parts;
    }

    public String formatEpisode(EpisodeInfo info) {
        return formatParts.stream()
                .map(p -> formatEpisodePartToString(p, info))
                .reduce(String::concat)
                .orElse("");
    }

    private String formatEpisodePartToString(FormatPart part, EpisodeInfo metadata) {
        String value;
        if(part.isSpecial()) {
            value = doEpisodeSubstitution(part.getValue(), metadata);

            value = doReplacement(part.getValue(), value);
        } else {
            value = part.getValue();
        }

        return value;
    }

    private String doEpisodeSubstitution(String value, EpisodeInfo info) {
        if(value.startsWith("seriesTitle")) {
            return info.getSeriesInfo().getSeriesTitle();
        }
        if(value.startsWith("seriesYear")) {
            return Integer.toString(info.getSeriesInfo().getStartYear());
        }
        if(value.startsWith("seasonNumber")) {
            return getPrettyNumber(info.getSeason());
        }
        if(value.startsWith("episodeNumber")) {
            return getPrettyNumber(info.getEpisode());
        }
        if(value.startsWith("episodeTitle")) {
            return info.getEpisodeTitle();
        }

        return "";
    }

    public String formatMovie(MovieInfo info) {
        return formatParts.stream()
                .map(p -> formatMoviePartToString(p, info))
                .reduce(String::concat)
                .orElse("");
    }

    private String formatMoviePartToString(FormatPart part, MovieInfo info) {
        String value;
        if(part.isSpecial()) {
            value = doMovieSubstitution(part.getValue(), info);

            value = doReplacement(part.getValue(), value);
        } else {
            value = part.getValue();
        }

        return value;
    }

    private String doMovieSubstitution(String value, MovieInfo info) {
        if(value.startsWith("movieTitle")) {
            return info.getMovieTitle();
        }
        if(value.startsWith("movieYear")) {
            return Integer.toString(info.getMovieYear());
        }

        return "";
    }

    private String doReplacement(String instructions, String value) {
        if(instructions.contains(".replace(")) {
            String replaceAndAfter = instructions.split(".replace\\(")[1];
            String replacements = replaceAndAfter.split("\\)")[0];
            String[] replacementParts = replacements.split("'");
            String from = replacementParts[1];
            String to = replacementParts[3];

            return value.replace(from, to);
        }

        return value;
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
