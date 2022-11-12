package io.camunda.example.pollingquarzjob.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.example.pollingquarzjob.quarz.JobInfo;
import io.camunda.example.pollingquarzjob.quarz.QuartzJobService;
import io.camunda.example.pollingquarzjob.quarz.jobs.WaitForResultJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Slf4j
@OutboundConnector(
    name = "Quartz_Outbound_Connector",
    inputVariables = {"message", "authentication"},
    type = "io.camunda:quartz:1")
public class QuartzOutboundConnectorFunction implements OutboundConnectorFunction {
  @Autowired
  QuartzJobService quartzJobService;

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var connectorRequest = context.getVariablesAsType(QuartzOutboundConnectorRequest.class);

    // TODO: add back with 0.3.0-alpha5
    // context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);

    // TODO: implement connector logic
    log.info("Executing Quartz connector with request {}", connectorRequest);

    // TODO make parameters
    String jobName = "camundaJob";
    String jobGroup = "camundaJobGroup";

    JobInfo jobInfo = JobInfo.builder()
        .jobClass(WaitForResultJob.class.getName())
        .jobGroup(jobGroup)
        .jobName(jobName)
        .repeatTime(10000L)
        .description("job with message" + connectorRequest.getMessage())
        .build();

    //TODO data map, e.g. message
    quartzJobService.scheduleNewJob(jobInfo, new HashMap());

    var result = new QuartzOutboundConnectorResult();

    log.info("Message  {}", connectorRequest.getMessage());

    result.setJobId("100");
    return result;
  }
}
