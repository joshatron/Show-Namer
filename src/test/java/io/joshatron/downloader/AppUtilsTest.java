package io.joshatron.downloader;

import io.joshatron.downloader.series.SeriesInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppUtilsTest {
    @Test
    public void prettyNumberLessThan10() {
        Assert.assertEquals(AppUtils.getPrettyNumber(5), "05");
    }

    @Test
    public void prettyNumberMoreThan10() {
        Assert.assertEquals(AppUtils.getPrettyNumber(12), "12");
    }

    @Test
    public void getExtensionNormalFile() {
        Assert.assertEquals(AppUtils.getExtension("file.mkv"), "mkv");
        Assert.assertEquals(AppUtils.getExtension("other_file.mp4"), "mp4");
    }

    @Test
    public void getExtensionMultiplePeriods() {
        Assert.assertEquals(AppUtils.getExtension("file.name.avi"), "avi");
    }

    @Test
    public void getExtensionNoExtenstion() {
        Assert.assertEquals(AppUtils.getExtension("file_name"), "");
    }

    @Test
    public void getExtensionFullFilePath() {
        Assert.assertEquals(AppUtils.getExtension("/home/user/file/path/file.mkv"), "mkv");
    }

    @Test
    public void formatEpisodeNoSubs() {
        String format = "hello world";
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 1;
        String episodeTitle = "C";
        Assert.assertEquals(AppUtils.formatEpisode(info, season, episode, episodeTitle, format),
                format);
    }
}