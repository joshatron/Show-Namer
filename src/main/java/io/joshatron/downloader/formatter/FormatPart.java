package io.joshatron.downloader.formatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormatPart {
    private boolean special = false;
    private String value;
}
