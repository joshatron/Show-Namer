package io.joshatron.downloader;

import java.io.File;

public class EpisodeAndSeasonPicker {
    public static int getSeason(String fileName) {
        String[] searchStrings = new String[]{"s", "S"};
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
            int nums = numsStarting(s);
            if(nums == 1 || nums == 2) {
                return Integer.parseInt(s.substring(0, nums));
            }
        }

        return -1;
    }

    private static int numsStarting(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return i;
            }
        }

        return str.length();
    }
}
