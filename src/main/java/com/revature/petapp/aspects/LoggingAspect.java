package com.revature.petapp.aspects;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component // marks this object as a Spring bean managed by the IOC container
@Aspect // marks this object as an Aspect class (not specific to Spring, but used by Spring AOP)
public class LoggingAspect {
	private Logger log;
	
	// if not using a reusable pointcut, you can just put the expression
	// right in the advice annotation
	@Around("execution(* com.revature..*(..) )")
	public Object log(ProceedingJoinPoint targetedMethod) throws Throwable {
		Object returnedObj = null;
		
		log = LogManager.getLogger(targetedMethod.getTarget().getClass());
		
		log.info("Method with signature: " + targetedMethod.getSignature());
		log.info("with arguments: " + Arrays.toString(targetedMethod.getArgs()));
		try {
			// if you don't do this, the actual method won't get called at all!
			// you'll basically stop all of your code from running.
			returnedObj = targetedMethod.proceed();
		} catch (Throwable t) {
			log.error(t.getMessage());
			log.warn(Arrays.toString(t.getStackTrace()));
			// if you don't do this, the exception won't get thrown properly,
			// which could break your code or cause exceptions to go unhandled!
			throw t;
		}
		
		log.info("returned: " + returnedObj);
		// if you don't do this, the actual returned object of your method
		// will not get returned. this could break your whole code.
		return returnedObj;
	}
}
