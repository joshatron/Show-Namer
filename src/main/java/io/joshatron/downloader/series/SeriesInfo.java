package io.joshatron.downloader.series;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriesInfo {
    private String seriesId;
    private String seriesTitle;
    private int startYear;

    public SeriesInfo() {
        seriesId = "";
        seriesTitle = "";
        startYear = 0;
    }
}
