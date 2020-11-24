package io.joshatron.downloader.backend;

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
