package io.camunda.example.pollingquartzjob.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.example.pollingquartzjob.quartz.JobInfo;
import io.camunda.example.pollingquartzjob.quartz.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@OutboundConnector(
    name = "Quartz_Outbound_Connector",
    inputVariables = {"dataMap","jobInfo", "authentication"},
    type = "io.camunda:quartz:1")
public class QuartzOutboundConnectorFunction implements OutboundConnectorFunction {
  @Autowired
  QuartzJobService quartzJobService;

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var request = context.getVariablesAsType(QuartzOutboundConnectorRequest.class);

    // TODO: add back with 0.3.0-alpha5
    // context.validate(connectorRequest);
    context.replaceSecrets(request);

    log.info("Executing Quartz connector with request {}", request);
    JobInfo jobInfo = request.getJobInfo();
    JobDataMap jobDataMap = new JobDataMap(request.getDataMap());
    log.debug("Scheduling job with {} and {}", jobInfo, jobDataMap.getWrappedMap());
    jobInfo.setCronExpression(null); // testing only. TODO: remove line
    JobDetail jobDetail = quartzJobService.scheduleNewJob(jobInfo, jobDataMap, null != jobInfo.getCronExpression());

    var result = new QuartzOutboundConnectorResult();
    result.setJobKey(jobDetail.getKey());

    return result;
  }
}
