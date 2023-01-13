package io.camunda.example.pollingquartzjob.quartz.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.text.ParseException;
import java.util.Date;

@Slf4j
public class JobScheduleCreator {

  public static JobDetail createJobDetail(Class<? extends QuartzJobBean> jobClass, boolean isDurable,
                             ApplicationContext context, String jobName, String jobGroup) {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(jobClass);
    factoryBean.setDurability(isDurable);
    factoryBean.setApplicationContext(context);
    factoryBean.setName(jobName);
    factoryBean.setGroup(jobGroup);

    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put(jobName + jobGroup, jobClass.getName());
    factoryBean.setJobDataMap(jobDataMap);
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }

  public static SimpleTrigger createSimpleTrigger(String triggerName, String triggerGroup,Date startTime, Long repeatInterval,int repeatCount,JobDataMap dataMap, int misFireInstruction) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName(triggerName);
    factoryBean.setGroup(triggerGroup);
    factoryBean.setStartTime(startTime);
    factoryBean.setRepeatInterval(repeatInterval);
    factoryBean.setRepeatCount(repeatCount);
    factoryBean.setMisfireInstruction(misFireInstruction);
    factoryBean.setJobDataMap(dataMap);
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }

  public static CronTrigger createCronTrigger(String triggerName, String triggerGroup, Date startTime, String cronExpression, JobDataMap dataMap, int misFireInstruction) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setName(triggerName);
    factoryBean.setGroup(triggerGroup);
    factoryBean.setStartTime(startTime);
    factoryBean.setCronExpression(cronExpression);
    factoryBean.setJobDataMap(dataMap);
    factoryBean.setMisfireInstruction(misFireInstruction);
    try {
      factoryBean.afterPropertiesSet();
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
    }
    return factoryBean.getObject();
  }

}
