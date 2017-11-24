package com.ef;


import com.ef.parser.DurationEnum;
import com.ef.parser.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Parser implements CommandLineRunner {

    @Autowired
    private LogService logService;

    @Value("${startDate:null}")
    private String startDateString;

    @Value("${duration:hourly}")
    private DurationEnum duration;

    @Value("${threshold:0}")
    private Integer threshold;

    @Value("${accesslog:.}")
    private String accesslog;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Parser.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n\n\n**** Execution parameters: ");
        System.out.println("\t--startDate=" + startDateString);
        System.out.println("\t--duration=" + duration);
        System.out.println("\t--threshold=" + threshold);
        System.out.println("\t--accesslog=" + accesslog);
    }
}

