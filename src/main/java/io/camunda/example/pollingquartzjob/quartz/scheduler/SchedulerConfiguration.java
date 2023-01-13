package io.camunda.example.pollingquartzjob.quartz.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Slf4j
@Configuration
public class SchedulerConfiguration {
  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private QuartzProperties quartzProperties;

   // Scheduler factory with custom job factory supporting autowiring of Spring beans into Jobs
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {

    // job factory to be used by scheduler
    AutowiringJobFactory jobFactory = new AutowiringJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    Properties properties = new Properties();
    properties.putAll(quartzProperties.getProperties());

    // scheduler factory configuration
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setJobFactory(jobFactory);
    schedulerFactory.setOverwriteExistingJobs(true);
//    schedulerFactory.setDataSource(dataSource);
    schedulerFactory.setQuartzProperties(properties);

    return schedulerFactory;
  }
}