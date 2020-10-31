package io.joshatron.downloader;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AppUtilsTest {
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
}