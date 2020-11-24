package io.joshatron.downloader.backend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriesInfo {
    private String seriesId;
    private String seriesTitle;
    private String seriesDescription;
    private int startYear;

    public SeriesInfo() {
        seriesId = "";
        seriesTitle = "";
        seriesDescription = "";
        startYear = 0;
    }
}
