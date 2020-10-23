package io.joshatron.downloader;

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
}
