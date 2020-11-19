package io.joshatron.downloader.formatter;

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

    public String formatEpisode(EpisodeMetadata metadata) {
        return formatParts.stream()
                .map(p -> formatPartToString(p, metadata))
                .reduce(String::concat)
                .orElse("");
    }

    private String formatPartToString(FormatPart part, EpisodeMetadata metadata) {
        String value;
        if(part.isSpecial()) {
            value = doSubstitution(part.getValue(), metadata);

            value = doReplacement(part.getValue(), value);
        } else {
            value = part.getValue();
        }

        return value;
    }

    private String doSubstitution(String value, EpisodeMetadata metadata) {
        if(value.startsWith("seriesTitle")) {
            return metadata.getSeriesInfo().getSeriesTitle();
        }
        if(value.startsWith("seriesYear")) {
            return Integer.toString(metadata.getSeriesInfo().getStartYear());
        }
        if(value.startsWith("seasonNumber")) {
            return getPrettyNumber(metadata.getSeason());
        }
        if(value.startsWith("episodeNumber")) {
            return getPrettyNumber(metadata.getEpisode());
        }
        if(value.startsWith("episodeTitle")) {
            return metadata.getEpisodeTitle();
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
