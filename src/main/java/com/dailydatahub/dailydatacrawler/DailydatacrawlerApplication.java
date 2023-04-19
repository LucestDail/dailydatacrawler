package com.dailydatahub.dailydatacrawler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DailydatacrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailydatacrawlerApplication.class, args);
	}

	// @Autowired
    // private JobLauncher jobLauncher;

    // @Autowired
    // private Job job;

    // @Bean
    // public CommandLineRunner commandLineRunner() {
    //     return args -> jobLauncher.run(job, new JobParameters());
    // }

}
