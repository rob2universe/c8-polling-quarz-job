package io.camunda.example.pollingquartzjob.quartz.jobs;

import io.camunda.example.pollingquartzjob.quartz.QuartzJobService;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@DisallowConcurrentExecution
public class WaitForResultJob extends QuartzJobBean {

  @Autowired
  ZeebeClientLifecycle client;

  @Autowired
  QuartzJobService jobService;

  @Override
  protected void executeInternal(JobExecutionContext context) {

    log.debug("Executing {} with context: {}", this.getClass().getName(), context);

    var dataMap = context.getMergedJobDataMap();
    log.debug("Payload {}", dataMap.getString("payload"));

    WebClient.ResponseSpec responseSpec = WebClient.create().get().uri("http://localhost:8080/status").retrieve();
    String responseBody = responseSpec.bodyToMono(String.class).block();
    log.debug("Job received result {}", responseBody);

    if (Boolean.parseBoolean(responseBody)) {
      String correlationKey = dataMap.getString("correlationKey");
      log.debug("Sending message with correlationKey {}", correlationKey);
      client.newPublishMessageCommand()
          .messageName("Msg_QuartzJobCompleted")
          .correlationKey(correlationKey)
          .send();

      try {
        log.info("Unscheduling Job with trigger key {}", context.getTrigger().getKey());
        jobService.unscheduleJob(context.getTrigger().getKey());
      } catch (SchedulerException e) {
        throw new RuntimeException(e);
      }
    } else {
      log.debug("Will check again at {}", context.getNextFireTime());
    }
  }
}

