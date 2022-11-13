package io.camunda.example.pollingquarzjob.quarz;

import io.camunda.example.pollingquarzjob.quarz.scheduler.JobScheduleCreator;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuartzJobService {
  public static final String TRIGGER_GROUP = "CamundaTriggers";
  private ApplicationContext applicationContext;
  private SchedulerFactoryBean schedulerFactory;

  public QuartzJobService(ApplicationContext applicationContext, SchedulerFactoryBean schedulerFactory) {
    this.applicationContext = applicationContext;
    this.schedulerFactory = schedulerFactory;
  }

  public JobDetail scheduleNewJob(JobInfo jobInfo, JobDataMap dataMap, boolean isCronJob) {
    JobDetail jobDetail=null;
    try {
      Scheduler scheduler = schedulerFactory.getScheduler();

      Class<? extends QuartzJobBean> jobClass = (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass());
      jobDetail = JobBuilder
          .newJob(jobClass)
          .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
          .usingJobData(dataMap)
          .build();

      if (!scheduler.checkExists(jobDetail.getKey())) {
        jobDetail = JobScheduleCreator.createJobDetail(
            jobClass, false, applicationContext,
            jobInfo.getJobName(), jobInfo.getJobGroup());

        Trigger trigger;
        String triggerName = jobInfo.getJobName();

        if (isCronJob) {
          log.debug("Creating CronTrigger: {}", triggerName);
          trigger = JobScheduleCreator.createCronTrigger(triggerName, TRIGGER_GROUP, jobInfo.getStartTime(),
              jobInfo.getCronExpression(), dataMap, CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        } else {
          log.debug("Creating SimpleTrigger: {}", triggerName);
          trigger = JobScheduleCreator.createSimpleTrigger(triggerName, TRIGGER_GROUP, jobInfo.getStartTime(),
              jobInfo.getRepeatInterval(),jobInfo.getRepeatCount(), dataMap, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
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
    return jobDetail;
  }

  public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
    return schedulerFactory.getScheduler().unscheduleJob(triggerKey);
  }
}
