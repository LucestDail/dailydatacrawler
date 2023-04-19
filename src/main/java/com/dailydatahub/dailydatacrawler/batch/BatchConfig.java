package com.dailydatahub.dailydatacrawler.batch;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Job job(Step step){
        return new JobBuilder("job", this.jobRepository).start(step).build();
    }

    @Bean
    public Step step(JobRepository jobRepository
                , Tasklet tasklet
                , PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step", jobRepository)
                .tasklet(tasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return (stepContribution, chunkContext) -> {
            log.info("<BATCH START> {}", dateFormat.format(new Date()));
            return RepeatStatus.FINISHED;
        };
    }
}

