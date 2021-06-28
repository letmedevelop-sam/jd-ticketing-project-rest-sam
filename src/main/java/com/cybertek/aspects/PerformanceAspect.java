package com.cybertek.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class PerformanceAspect {

  //  Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);  // after adding @Slf4j we deleted

    @Pointcut("@annotation(com.cybertek.annotation.ExecutionTime)")
    private void anyExecutionTimeOperation(){}      //method level  // run time

    @Around("anyExecutionTimeOperation()")
    public Object anyExecutionTimeOperationAdvice(ProceedingJoinPoint proceedingJoinPoint)  {

        long beforeTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        long afterTime = System.currentTimeMillis();

        log.info("Time taken to execute : {} ms (Method : {} - Parameters : {}",(afterTime-beforeTime),proceedingJoinPoint.getSignature().toShortString(),proceedingJoinPoint.getArgs());

        return result;
    }
}
