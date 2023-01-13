package io.camunda.example.pollingquartzjob.connector;

import lombok.Data;
import org.quartz.JobKey;

@Data
public class QuartzOutboundConnectorResult {

  private JobKey jobKey;

}
