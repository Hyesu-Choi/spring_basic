package com.beyond.basic.b2_board.post.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PostBatchJobs {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    @Autowired
    public PostBatchJobs(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job postJob(){
        return new JobBuilder("postJob", jobRepository )
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    @Bean
    public Step firstStep(){
        return new StepBuilder("firstStep", jobRepository)
                .tasklet((a,b) -> {
                    System.out.println("======batch task1 시작 =====");

                    System.out.println("======batch task1 끝 =====");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step secondStep(){
        return new StepBuilder("secondStep", jobRepository)
                .tasklet((a,b) -> {
                    System.out.println("======batch task2 시작 =====");

                    System.out.println("======batch task2 끝 =====");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
