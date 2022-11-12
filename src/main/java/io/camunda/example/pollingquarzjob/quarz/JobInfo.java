package io.camunda.example.pollingquarzjob.quarz;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class JobInfo {

  private String jobId;
  private String jobName;
  private String jobGroup;
  private String jobStatus;
  private String jobClass;
  private String cronExpression;
  private String description;
  private Long repeatTime;
}
