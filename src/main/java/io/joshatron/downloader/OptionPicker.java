package io.joshatron.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class OptionPicker {

    public static int pickOption(String search, List<String> titles, List<String> options) {
        int option = pickObviousOption(search, titles);
        if(option == -1) {
            option = pickOptionManual(options);
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
