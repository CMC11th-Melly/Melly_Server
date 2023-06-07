package cmc.mellyserver.mellybatch.common.scheduler;

import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleScheduler {

	@Autowired
	private Job inactiveUserJob;

	@Autowired
	private Job passwordChangeJob;

	@Autowired
	private JobLauncher jobLauncher;

	@Scheduled(cron = "0 */1 * * * *")
	public void passwordChangeJobRun() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {

		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis()))
		);

		jobLauncher.run(passwordChangeJob, jobParameters);
	}

	public void inactiveUserJobRun() {

	}
}
