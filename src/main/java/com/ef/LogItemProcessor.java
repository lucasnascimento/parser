package com.ef;

import com.ef.parser.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j
public class LogItemProcessor implements ItemProcessor<Log, Log> {

    @Override
    public Log process(final Log logItem) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(logItem.getDateString(), formatter);
        logItem.setDate(dateTime);

        return logItem;
    }


}
