package io.camunda.example.pollingquartzjob.quartz.scheduler;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutowiringJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

  private AutowireCapableBeanFactory factory;

  @Override
  public void setApplicationContext(final ApplicationContext context) {
    factory = context.getAutowireCapableBeanFactory();
  }

  @Override
  protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
    final Object job = super.createJobInstance(bundle);
    factory.autowireBean(job);
    return job;
  }
}