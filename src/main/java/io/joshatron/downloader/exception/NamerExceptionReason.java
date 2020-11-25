package io.joshatron.downloader.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NamerExceptionReason {
    BACKEND_NOT_RECOGNIZED("The given backend is not recognized as a legal option."),
    FAILED_TO_LOAD_PROPERTIES("Could not load the application properties.");

    private final String reasonString;
}
