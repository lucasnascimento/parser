package com.ef.parser;

import java.time.Duration;
import java.time.LocalDateTime;

public enum DurationEnum {

    hourly(Duration.ofHours(1)),
    daily(Duration.ofDays(1));

    Duration duration;

    DurationEnum(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime plusDuration(LocalDateTime localDateTime) {
        return localDateTime.plus(duration);
    }

}
