package io.joshatron.downloader;

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
}