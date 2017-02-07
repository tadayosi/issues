package com.redhat.issues.amq;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource("/beans.xml")
public class SpringAmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAmqApplication.class, args);
    }

}
