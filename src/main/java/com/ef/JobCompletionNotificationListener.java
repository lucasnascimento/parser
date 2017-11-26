package com.ef;

import com.ef.parser.BlockedRepository;
import com.ef.parser.DurationEnum;
import com.ef.parser.LogRepository;
import com.ef.parser.LogService;
import lombok.extern.log4j.Log4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Log4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Value("${startDate:null}")
    private String startDateString;

    @Value("${duration:hourly}")
    private DurationEnum duration;

    @Value("${threshold:0}")
    private Integer threshold;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private BlockedRepository blockedRepository;

    @Autowired
    private LogService logService;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("FINISHED! Time to verify the results");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(startDateString, formatter);
            logService.checkBlocked(startDate, duration, threshold);
            long count = blockedRepository.count();
            System.out.println(String.format("Were blocked %s ip at this moment.", count));
            blockedRepository.findAll().forEach(System.out::println);
        }
    }
}

