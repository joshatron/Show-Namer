package io.joshatron.downloader.formatter;

import io.joshatron.downloader.metadata.SeriesInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FormatterTest {
    @Test
    public void formatEpisodeNoSubs() {
        String format = "hello world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                format);
    }

    @Test
    public void formatEpisodeSubSeriesTitle() {
        String format = "hello {seriesTitle} world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "hello B world");
    }

    @Test
    public void formatEpisodeSubEpisodeTitle() {
        String format = "hello {episodeTitle} world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "hello C world");
    }

    @Test
    public void formatEpisodeSubSeasonNumber() {
        String format = "hello S{seasonNumber} world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "hello S01 world");
    }

    @Test
    public void formatEpisodeSubEpisodeNumber() {
        String format = "hello E{episodeNumber} world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "hello E02 world");
    }

    @Test
    public void formatEpisodeSubShowYear() {
        String format = "hello {seriesYear} world";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "hello 1 world");
    }

    @Test
    public void formatEpisodeSubMultiple() {
        String format = "{seriesTitle}.{seriesYear}:S{seasonNumber}E{episodeNumber}:{episodeTitle}";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "C";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "B.1:S01E02:C");
    }

    @Test
    public void formatEpisodeWithReplaceMacro() {
        String format = "{seriesTitle.replace(' ', '_')}.{seriesYear}:S{seasonNumber}E{episodeNumber}:{episodeTitle.replace(' ','-'}";
        Formatter formatter = new Formatter(format);
        SeriesInfo info = new SeriesInfo("A", "B C", "D", 1);
        int season = 1;
        int episode = 2;
        String episodeTitle = "D E";
        Assert.assertEquals(formatter.formatEpisode(new EpisodeMetadata(info, season, episode, episodeTitle)),
                "B_C.1:S01E02:D-E");
    }
}