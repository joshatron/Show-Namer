package io.joshatron.downloader.formatter;

import io.joshatron.downloader.metadata.SeriesInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpisodeMetadata {
    private SeriesInfo seriesInfo;
    private int season;
    private int episode;
    private String episodeTitle;
}
