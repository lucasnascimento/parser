package com.ef.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private BlockedRepository blockedRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkBlocked(final LocalDateTime startDate, final DurationEnum duration, final Integer threshold) {
        final String sql = "select ip, count(*) from log where date between ? and ? group by ip having count(*) > ?";
        final LocalDateTime endDate = duration.plusDuration(startDate);
        List<Blocked> ipsToBlock = jdbcTemplate.query(sql, new Object[]{startDate, endDate, threshold}, (rs, row) -> {
            String reasonString = String.format("This ip exceed the %s limit of %s between %s and %s. Total requests are %s .",
                    duration, threshold, startDate, endDate, rs.getInt(2));
            return new Blocked(rs.getString(1), reasonString);
        });
        blockedRepository.save(ipsToBlock);

        return true;
    }

}
