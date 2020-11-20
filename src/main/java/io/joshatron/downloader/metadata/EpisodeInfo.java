package io.joshatron.downloader.metadata;

import io.joshatron.downloader.metadata.SeriesInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpisodeInfo {
    private SeriesInfo seriesInfo;
    private int season;
    private int episode;
    private String episodeTitle;
}
