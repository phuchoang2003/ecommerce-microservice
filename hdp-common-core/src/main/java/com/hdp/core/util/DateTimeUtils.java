package com.hdp.core.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static Instant nowInstant() {
        return Instant.now();
    }

    public static ZonedDateTime parse(String dateTime) {
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static String format(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static String format(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static ZonedDateTime toVietnamTime(Instant instant) {
        return instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}