package io.camunda.example.pollingquarzjob.quarz;

import io.camunda.example.pollingquarzjob.quarz.jobs.JobScheduleCreator;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class QuartzJobService {
  private ApplicationContext applicationContext;
  private SchedulerFactoryBean schedulerFactory;
  private JobScheduleCreator scheduleCreator;

  public QuartzJobService(ApplicationContext applicationContext, SchedulerFactoryBean schedulerFactory, JobScheduleCreator scheduleCreator) {
    this.applicationContext = applicationContext;
    this.schedulerFactory = schedulerFactory;
    this.scheduleCreator = scheduleCreator;
  }

  /* public void startJob(String jobName, String jobGroup, Map dataMap) throws SchedulerException {

      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("message", dataMap.get("message"));

      schedulerFactory.getScheduler().triggerJob(new JobKey(jobName, jobGroup), jobDataMap);
      log.info(" job {} scheduled in job group {} with jobDataMap {}", jobName, jobGroup, jobDataMap);

    }
  */
  public void scheduleNewJob(JobInfo jobInfo, JobDataMap dataMap, boolean isCronJob) {
    try {
      Scheduler scheduler = schedulerFactory.getScheduler();

      JobDetail jobDetail = JobBuilder
          .newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
          .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
          .usingJobData(dataMap)
          .build();
      if (!scheduler.checkExists(jobDetail.getKey())) {

        jobDetail = scheduleCreator.createJobDetail(
            (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, applicationContext,
            jobInfo.getJobName(), jobInfo.getJobGroup());

        Trigger trigger;
        if (isCronJob) {
          trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
              jobInfo.getCronExpression(), dataMap, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
          trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
              jobInfo.getRepeatTime(), dataMap, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        scheduler.scheduleJob(jobDetail, trigger);

        log.info("Job scheduled wit JobInfo {}", jobInfo);
      } else {
        log.error("Job already exists: {}", jobInfo);
      }
    } catch (ClassNotFoundException e) {
      log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
    } catch (SchedulerException e) {
      log.error(e.getMessage(), e);
    }
  }
}
