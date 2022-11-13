package io.camunda.example.pollingquarzjob.quarz.jobs;

import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@DisallowConcurrentExecution
public class WaitForResultJob extends QuartzJobBean {

  @Autowired
  ZeebeClientLifecycle client;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    log.info("JobExecutionContext: {}", context);

    //call REST service and wai for response
    WebClient.ResponseSpec responseSpec = WebClient.create().get().uri("http://localhost:8080/status").retrieve();
    String responseBody = responseSpec.bodyToMono(String.class).block();

    log.info("Job received result {}", responseBody);

    var dataMap = context.getMergedJobDataMap();

    if(Boolean.parseBoolean(responseBody)){
      String correlationKey = dataMap.getString("correlationKey");
      log.debug("Sending message with correlationKey {}", correlationKey);
      client.newPublishMessageCommand()
          .messageName("Msg_QuartzJobCompleted")
          .correlationKey(correlationKey)
          .send();
      // TODO cancel job
    }
    else{
      log.debug("Will check again at {}", context.getNextFireTime());
    }
  }
}

