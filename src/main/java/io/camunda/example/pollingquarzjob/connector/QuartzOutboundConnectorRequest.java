package io.camunda.example.pollingquarzjob.connector;

import io.camunda.connector.api.annotation.Secret;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Objects;

public class QuartzOutboundConnectorRequest {

  @NotEmpty
  private String message;

  private String jobType = "waitForTime";

  @Valid
  @NotNull
  @Secret
  private Authentication authentication;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }

  @Override
  public int hashCode() {
    return Objects.hash(authentication, message);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    QuartzOutboundConnectorRequest other = (QuartzOutboundConnectorRequest) obj;
    return Objects.equals(authentication, other.authentication)
        && Objects.equals(message, other.message);
  }

  @Override
  public String toString() {
    return "QuartzOutboundConnectorRequest [message=" + message + ", authentication=" + authentication + "]";
  }
}
