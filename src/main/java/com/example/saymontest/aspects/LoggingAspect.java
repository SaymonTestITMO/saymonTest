package com.example.saymontest.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(com.example.saymontest.aspects.annotations.Loggable)")
    public void loggableMethods() {
    }

    @Before("loggableMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("Entering method: {} with args {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "loggableMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "loggableMethods()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        log.info("Exception in method {} : {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }
}

