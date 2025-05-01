package com.example.saymontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SaymonTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaymonTestApplication.class, args);
    }

}
