package io.joshatron.downloader;

import io.joshatron.downloader.backend.MovieInfo;
import io.joshatron.downloader.backend.SeriesInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class OptionPicker {

    public static int pickOptionSeries(String search, List<SeriesInfo> options) {
        List<String> titles = options.stream()
                .map(SeriesInfo::getSeriesTitle)
                .collect(Collectors.toList());
        int option = pickObviousOption(search, titles);
        if(option == -1) {
            List<String> choices = options.stream()
                    .map(o -> o.getSeriesTitle() + " (" + o.getSeriesId() + "), " + o.getStartYear() + ": " + o.getSeriesDescription())
                    .collect(Collectors.toList());
            option = pickOptionManual(choices);
        }

        return option;
    }

    public static int pickOptionMovie(String search, List<MovieInfo> options) {
        List<String> titles = options.stream()
                .map(MovieInfo::getMovieTitle)
                .collect(Collectors.toList());
        int option = pickObviousOption(search, titles);
        if(option == -1) {
            List<String> choices = options.stream()
                    .map(o -> o.getMovieTitle() + " (" + o.getMovieId() + "), " + o.getMovieYear() + ": " + o.getMovieDescription())
                    .collect(Collectors.toList());
            option = pickOptionManual(choices);
        }

        return option;
    }

    private static int pickObviousOption(String search, List<String> titles) {
        int choice = -1;
        for(int i = 0; i < titles.size(); i++) {
            if(search.equalsIgnoreCase(titles.get(i))) {
                if(choice == -1) {
                    choice = i;
                } else {
                    return -1;
                }
            }
        }

        return choice;
    }

    private static int pickOptionManual(List<String> options) {
        System.out.println("Multiple matches found, please choose from below options:");

        int i = 1;
        for(String option : options) {
            System.out.println(i + ") " + option);
            i++;
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Choice: ");
            int option = Integer.parseInt(br.readLine().trim());

            return option - 1;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
