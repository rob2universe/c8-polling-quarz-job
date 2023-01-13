package io.camunda.example.pollingquartzjob.connector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorFactory {
  @Bean
  public QuartzOutboundConnectorFunction quartzOutboundConnectorFunction() {
    return new QuartzOutboundConnectorFunction();
  }
}

