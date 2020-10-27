package io.joshatron.downloader.metadata;

import java.util.List;

public interface SeriesInterface {
    String guessSeriesIdFromName(String showName);
    List<SeriesInfo> getAllSeriesFromName(String showName);
    SeriesInfo getSeriesInfo(String seriesId);
    String getEpisodeTitle(String seriesId, int season, int episode);
}
