package io.camunda.example.pollingquartzjob.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@Slf4j
@RestController
public class SomeRestService {

//  will return true for 1 minute every 3 minutes
  @GetMapping("/status")
  public boolean getStatus()
  {
     boolean result = LocalDateTime.now().getMinute() % 3 == 0;
     log.trace("Status returned by REST Service {}", result);
     return result;
  }
}
