package io.joshatron.downloader;

import java.io.File;

public class EpisodeAndSeasonPicker {
    public static int getSeason(String fileName) {
        String[] searchStrings = new String[]{"s", "S", "season", "Season"};
        String justFile = removePathFromFileName(fileName);

        for(String searchString : searchStrings) {
            int result = searchNumAfterString(justFile, searchString);

            if(result != -1) {
                return result;
            }
        }

        return -1;
    }

    private static String removePathFromFileName(String fullPath) {
        File file = new File(fullPath);
        return file.getName();
    }

    private static int searchNumAfterString(String fileName, String searchString) {
        String[] ss = fileName.split(searchString);
        for(String s : ss) {
            int season = getSeasonFromSubstring(s);
            if(season != -1) {
                return season;
            }
        }

        return -1;
    }

    private static int getSeasonFromSubstring(String str) {
        int i = 0;

        while(i < str.length() && isWhiteSpace(str.charAt(i))) {
            i++;
        }

        int start = i;
        int digits = 0;
        while(i < str.length() && isDigit(str.charAt(i))) {
            i++;
            digits++;
        }

        if(digits > 0) {
            return Integer.parseInt(str.substring(start, start + digits));
        }

        return -1;
    }

    private static boolean isWhiteSpace(char c) {
        return c == ' ' || c == '_' || c == '-';
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }
}
