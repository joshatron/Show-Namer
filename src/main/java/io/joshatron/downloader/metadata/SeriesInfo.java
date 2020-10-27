package io.joshatron.downloader.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriesInfo {
    private String seriesId;
    private String seriesTitle;
    private int startYear;
}
