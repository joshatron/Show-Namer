package io.joshatron.downloader;

import org.apache.commons.lang3.SystemUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EpisodeAndSeasonPickerTest {
    @Test
    public void getSeasonNoWayToKnow() {
        String file = "/path/to/episode/file.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, -1);
    }

    @Test
    public void getSeasonLowercaseSInFileName() {
        String file = "/path/to/episode/Show:s05e01:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 5);
    }

    @Test
    public void getSeasonUppercaseSInFileName() {
        String file = "/path/to/episode/Show:S3E1:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 3);
    }

    @Test
    public void getSeasonWrongEarlierInPath() {
        String file = "";
        if(SystemUtils.IS_OS_WINDOWS) {
            file = "C:\\path\\with\\bad\\S02\\when\\its\\S04E06.mp4";
        } else {
            file = "/path/with/bad/S02/when/its/S04E06.mp4";
        }

        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 4);
    }
}