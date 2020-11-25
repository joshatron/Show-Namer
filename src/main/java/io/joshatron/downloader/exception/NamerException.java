package io.joshatron.downloader.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NamerException extends RuntimeException {

    private final NamerExceptionReason reason;

    @Override
    public String getMessage() {
        return reason.getReasonString();
    }
}
