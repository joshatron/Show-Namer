package io.joshatron.downloader.formatter;

import io.joshatron.downloader.AppUtils;
import io.joshatron.downloader.series.SeriesInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EpisodeFormatterTest {
    @Test
    public void formatEpisodeNoSubs() {
        String format = "hello world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                format);
    }

    @Test
    public void formatEpisodeSubSeriesTitle() {
        String format = "hello {seriesTitle} world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "hello B world");
    }

    @Test
    public void formatEpisodeSubEpisodeTitle() {
        String format = "hello {episodeTitle} world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "hello C world");
    }

    @Test
    public void formatEpisodeSubSeasonNumber() {
        String format = "hello S{season} world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "hello S01 world");
    }

    @Test
    public void formatEpisodeSubEpisodeNumber() {
        String format = "hello E{episode} world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "hello E02 world");
    }

    @Test
    public void formatEpisodeSubShowYear() {
        String format = "hello {seriesYear} world";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "hello 1 world");
    }

    @Test
    public void formatEpisodeSubMultiple() {
        String format = "{seriesTitle}.{seriesYear}:S{season}E{episode}:{episodeTitle}";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "B.1:S01E02:C");
    }

    @Test
    public void formatEpisodeWithReplaceMacro() {
        String format = "{seriesTitle.replace(' ', '_')}.{seriesYear}:S{season}E{episode}:{episodeTitle.replace(' ','-'}";
        EpisodeFormatter formatter = new EpisodeFormatter(format);
        SeriesInfo info = new SeriesInfo("A", "B C", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "D E";
        Assert.assertEquals(formatter.formatEpisode(info, season, episode, episodeTitle),
                "B_C.1:S01E02:D-E");
    }
}