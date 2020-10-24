package io.joshatron.downloader;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpisodeInfo {
    private String showName;
    private int season;
    private int episode;
    private String episodeName;

    public EpisodeInfo() {
        showName = "";
        season = 0;
        episode = 0;
        episodeName = "";
    }
}
