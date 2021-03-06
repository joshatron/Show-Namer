package io.joshatron.downloader.backend;

import java.util.List;

public interface SeriesInterface {
    List<SeriesInfo> searchSeriesName(String seriesName);
    SeriesInfo getSeriesInfo(String seriesId);
    String getEpisodeTitle(String seriesId, int season, int episode);
}
