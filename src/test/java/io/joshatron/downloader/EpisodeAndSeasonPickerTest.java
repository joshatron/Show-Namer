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
    public void getSeasonSpaceBetweenSAndNumber() {
        String file = "/path/to/episode/Show:S 17E1:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 17);
    }

    @Test
    public void getSeasonUnderscoreBetweenSAndNumber() {
        String file = "/path/to/episode/Show:s_6e1:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 6);
    }

    @Test
    public void getSeasonDashBetweenSeasonAndNumber() {
        String file = "/path/to/episode/Show:Season- 09e1:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 9);
    }

    @Test
    public void getSeasonLowercaseSeason() {
        String file = "/path/to/episode/Show:season-002e1:Episode_Name.mkv";
        int season = EpisodeAndSeasonPicker.getSeason(file);

        Assert.assertEquals(season, 2);
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

    @Test
    public void getEpisodeNoWayToKnow() {
        String file = "/path/to/episode/file.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, -1);
    }

    @Test
    public void getEpisodeLowercaseE() {
        String file = "/path/to/episode/Show:s05e06:Episode_Name.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, 6);
    }

    @Test
    public void getEpisodeUppercaseE() {
        String file = "/path/to/episode/Show - S02E057.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, 57);
    }

    @Test
    public void getEpisodeLowercaseEpisode() {
        String file = "/path/to/episode/Season_05/episode 10- The episode name.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, 10);
    }

    @Test
    public void getEpisodeUppercaseEpisode() {
        String file = "/path/to/episode/Season_05/Episode_100-_The_episode_name.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, 100);
    }

    @Test
    public void getEpisodeOnlyInFileName() {
        String file = "/path/to/E02/Season_05/E07-_The_episode_name.mkv";
        int episode = EpisodeAndSeasonPicker.getEpisode(file);

        Assert.assertEquals(episode, 7);
    }
}