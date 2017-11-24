package com.ef;

import com.ef.parser.Log;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Value("${accesslog:.}")
    private String accesslog;

    @Bean
    public Job importLogJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importLogJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Log, Log>chunk(50000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Log> writer() {
        JdbcBatchItemWriter<Log> writer = new JdbcBatchItemWriter<Log>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO log (date, ip, request, status, user_agent) VALUES (:date, :ip, :request, :status, :userAgent)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public LogItemProcessor processor() {
        return new LogItemProcessor();
    }

    @Bean
    public ItemReader<Log> reader() {
        FlatFileItemReader<Log> logFileReader = new FlatFileItemReader<>();
        logFileReader.setResource(new FileSystemResource(accesslog));
        logFileReader.setLinesToSkip(1);

        LineMapper<Log> logLineMapper = createLogLineMapper();
        logFileReader.setLineMapper(logLineMapper);

        return logFileReader;
    }

    private LineMapper<Log> createLogLineMapper() {
        DefaultLineMapper<Log> logLineMapper = new DefaultLineMapper<>();

        LineTokenizer logLineTokenizer = createLogLineTokenizer();
        logLineMapper.setLineTokenizer(logLineTokenizer);

        FieldSetMapper<Log> logInformationMapper = createLogInformationMapper();
        logLineMapper.setFieldSetMapper(logInformationMapper);

        return logLineMapper;
    }

    private LineTokenizer createLogLineTokenizer() {
        DelimitedLineTokenizer logLineTokenizer = new DelimitedLineTokenizer();
        logLineTokenizer.setDelimiter("|");
        logLineTokenizer.setNames(new String[]{"dateString", "ip", "request", "status", "userAgent"});
        return logLineTokenizer;
    }

    private FieldSetMapper<Log> createLogInformationMapper() {
        BeanWrapperFieldSetMapper<Log> logInformationMapper = new BeanWrapperFieldSetMapper<>();
        logInformationMapper.setTargetType(Log.class);
        return logInformationMapper;
    }
}