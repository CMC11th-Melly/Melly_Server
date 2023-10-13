package cmc.mellyserver.mellybatch.common.scheduler;

import org.springframework.stereotype.Component;

@Component
public class SampleScheduler {
//
//    @Autowired
//    private Job inactiveUserJob;
//
//    @Autowired
//    private Job passwordChangeJob;
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Scheduled(cron = "0 */1 * * * *")
//    public void passwordChangeJobRun() throws
//            JobInstanceAlreadyCompleteException,
//            JobExecutionAlreadyRunningException,
//            JobParametersInvalidException,
//            JobRestartException {
//
//        JobParameters jobParameters = new JobParameters(
//                Collections.singletonMap("requestTime",
//                        new JobParameter(System.currentTimeMillis()))
//        );
//
//        jobLauncher.run(passwordChangeJob, jobParameters);
//    }
//
//    @Scheduled(cron = "0 */1 * * * *")
//    public void inactiveUserJobRun()
//            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//
//        JobParameters jobParameters = new JobParameters(
//                Collections.singletonMap("requestTime",
//                        new JobParameter(System.currentTimeMillis()))
//        );
//
//        jobLauncher.run(inactiveUserJob, jobParameters);
//    }
}
