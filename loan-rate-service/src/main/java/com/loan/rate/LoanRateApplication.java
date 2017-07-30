package com.loan.rate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@SpringBootApplication
@Profile("app")
@ComponentScan("com.loan")
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class LoanRateApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder().sources(LoanRateApplication.class)
      .profiles("app", "health")
      .run(args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
//  @Bean
//  ObjectMapper customizeJacksonConfiguration() {
//      ObjectMapper om = new ObjectMapper();
//      om.registerModule(new GuavaModule());
//      return om;
//  }
//  
  @Bean
  public Module guavaModule() {
      return new GuavaModule();
  }

}
