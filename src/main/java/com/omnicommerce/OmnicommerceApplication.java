package com.omnicommerce;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class OmnicommerceApplication {
  public static void main(String[] args) {
    SpringApplication.run(OmnicommerceApplication.class, args);
  }
}
