package io.camunda.example.pollingquartzjob.connector;

import io.camunda.connector.api.annotation.Secret;
import io.camunda.example.pollingquartzjob.quartz.JobInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
public class QuartzOutboundConnectorRequest {

  @Valid
  @NotNull
  @Secret
  private Authentication authentication;

  @NotNull
  private JobInfo jobInfo;

  private Map dataMap;

}
