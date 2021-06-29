package com.cybertek.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Configuration
@Slf4j      //when we add thia annotation we dont need to initialize the logger
public class LoggingAspect {

/*
    AOP Use Cases
• Most Common
    - Logging, security
• Audit Logging
    - Who, what, when, where
• Exception Handling
    - Log exception and notify DevOps team via email
• API Management
    - How many times has a method been called user
    - Analytics: what are peak times? What is average load?
*/

/*
    • Aspect      : Module of code for a cross-cutting concern (logging, security)
    • Advice      : What action is taken and when it should be applied
    • Joint Point : It's a particular point during execution of programs like method execution,
    constructor call, or field assignment
    • Pointcut    : A regular expression that matches a joinpoint. (Where to implement the code)
    Each time any join point matches a pointcut, a specified advice associated with that pointcut is executed
    • Weaving – the process of linking aspects with targeted objects to create an advised object
 */

/*
    Advice Types
    • Before advice : run before the method
    • After finally advice : run after the method (finally)
    • After returning advice : run after method (success execution)
    • After throwing advice : run after method (if exception thrown)
 */

/*
    Spring AOP RoadMap
    • Create Aspects
    • Develop Advices
        - Before, After returning, After throwing
        - After finally, Around
    • Create Pointcut expressions
    • Apply it to our application
 */
    //initialize the logger
//    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // @Pointcut sets : Where-when-what to implement in the code
    @Pointcut("execution(* com.cybertek.controller.ProjectController.*(..)) || execution(* com.cybertek.controller.TaskController.*(..))")   //each method - everywhere in the controller
    private void anyControllerOperation(){}
    @Before("anyControllerOperation()")
    public void anyBeforeControllerOperationAdvice(JoinPoint joinPoint){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Before(User: {} Method : {} - Parameters : {}", auth.getName(), joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "anyControllerOperation()",returning = "results")
    public void anyAfterReturningControllerOperationAdvice(JoinPoint joinPoint, Object results){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("AfterReturning(User : {} Method : {} - Results : {}", auth.getName(), joinPoint.getSignature().toShortString(), results.toString());
    }

    @AfterThrowing(pointcut = "anyControllerOperation()", throwing = "exception")
    public void anyAfterThrowingControllerOperationAdvice(JoinPoint joinPoint,RuntimeException exception){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("After throwing(User : {} Method : {} - Exception : {}", auth.getName(), joinPoint.getSignature().toShortString(), exception.getLocalizedMessage());
    }


}
