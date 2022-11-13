package io.camunda.example.pollingquarzjob.quarz;


import lombok.Data;

import java.util.Date;

@Data
public class JobInfo {
  private String jobName;
  private String jobGroup;
  private String jobClass;
  private String cronExpression;
  private String description;
  private long repeatInterval;
  private int repeatCount;
  private Date startTime;
}
