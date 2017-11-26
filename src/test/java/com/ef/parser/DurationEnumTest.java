package com.ef.parser;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class DurationEnumTest {

    @Test
    public void must_have_one_hour_interval_on_hourly_test() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAhead = now.plusHours(1);

        Assert.assertEquals(oneHourAhead, DurationEnum.hourly.plusDuration(now));
    }

    @Test
    public void must_have_one_day_interval_on_daily_test() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAhead = now.plusDays(1);

        Assert.assertEquals(oneDayAhead, DurationEnum.daily.plusDuration(now));
    }

}