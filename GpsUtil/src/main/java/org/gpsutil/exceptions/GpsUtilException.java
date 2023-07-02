package org.gpsutil.exceptions;

public class GpsUtilException extends RuntimeException {

    public GpsUtilException(String message) {
        super(message);
    }

    public GpsUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
