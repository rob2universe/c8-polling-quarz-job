package io.camunda.example.pollingquarzjob.connector;

import java.util.Objects;

public class QuartzOutboundConnectorResult {

  // TODO: define connector result properties, which are returned to the process engine
  private String jobId;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final QuartzOutboundConnectorResult that = (QuartzOutboundConnectorResult) o;
    return Objects.equals(jobId, that.jobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId);
  }

  @Override
  public String toString() {
    return "QuartzOutboundConnectorResult [jobId=" + jobId + "]";
  }

}
