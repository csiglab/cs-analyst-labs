package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ExecutionTimeAspect {

    @Around("execution(* com.example..*(..))") // Applies to all methods in the package
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object result = joinPoint.proceed(); // Execute the actual method

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        
        System.out.println("Method " + joinPoint.getSignature().getName() + " executed in " + executionTime + " ns");
        
        return result;
    }
}
