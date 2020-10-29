package io.joshatron.downloader;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpisodeInfo {
    private String showTitle;
    private int season;
    private int episode;
    private String episodeTitle;

    public EpisodeInfo() {
        showTitle = "";
        season = 0;
        episode = 0;
        episodeTitle = "";
    }
}
