package ru.jpa.utils.specification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TestRunner {

  public static void main(String[] args) {
    SpringApplication.run(TestRunner.class, args);
  }

}
