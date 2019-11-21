package com.dubbo.mock.test;

import com.dubbo.mock.test.context.ApplicationReadyEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DubboMockApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(DubboMockApplication.class);
        springApplication.addListeners(new ApplicationReadyEventListener()); // load jars when startup
        springApplication.run(args);
        log.info("service started...");
    }
}

