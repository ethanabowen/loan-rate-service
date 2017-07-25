package com.loan.rate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Profile("app")
@ComponentScan("com.wh")
@EnableConfigurationProperties
@EnableMongoRepositories
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

}
