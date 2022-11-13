package io.camunda.example.pollingquarzjob.connector;

import lombok.Data;
import org.quartz.JobKey;

import java.util.Objects;

@Data
public class QuartzOutboundConnectorResult {

  private JobKey jobKey;

}
