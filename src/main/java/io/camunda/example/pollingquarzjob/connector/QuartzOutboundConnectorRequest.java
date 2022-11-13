package io.camunda.example.pollingquarzjob.connector;

import io.camunda.connector.api.annotation.Secret;
import io.camunda.example.pollingquarzjob.quarz.JobInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@NoArgsConstructor
public class QuartzOutboundConnectorRequest {

  @Valid
  @NotNull
  @Secret
  private Authentication authentication;

  @NotNull
  private JobInfo jobInfo;

  private HashMap dataMap;

}
