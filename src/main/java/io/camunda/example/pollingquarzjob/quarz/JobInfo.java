package io.camunda.example.pollingquarzjob.quarz;


import lombok.Data;

@Data
public class JobInfo {

  private String jobId;
  private String jobName;
  private String jobGroup;
  private String jobStatus;
  private String jobClass;
  private String cronExpression;
  private String description;
  //TODO change to repeatInterval
  private Long repeatTime;
}
