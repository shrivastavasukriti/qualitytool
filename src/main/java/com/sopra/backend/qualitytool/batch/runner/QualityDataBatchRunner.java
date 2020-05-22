package com.sopra.backend.qualitytool.batch.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class QualityDataBatchRunner {
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("job1")
	Job job1;
	
	@Autowired
	@Qualifier("job2")
	Job job2;

	@Scheduled(cron = "0 */1 * * * ?")
	public String handle() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong(
					"time", System.currentTimeMillis()).toJobParameters();
			jobLauncher.run(job1, jobParameters);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return "Done";
	}
	
	@Scheduled(cron = "0 */3 * * * ?")
	public String second() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong(
					"time", System.currentTimeMillis()).toJobParameters();
			jobLauncher.run(job2, jobParameters);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return "Done";
	}
}
