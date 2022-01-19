package com.revature.petapp.aspects;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.petapp.beans.Pet;

@Component // tells Spring to manage this as a Spring bean
@Aspect // says this is an aspect class
public class ExampleAspect {
	// advice needs a pointcut expression so that it knows which
	// joinpoints it needs to target
	
	// we can have a reusable pointcut expression if we're using
	// multiple advice methods in the same aspect
	// OR we can specify the pointcut expression for each individual
	// advice method using the advice's annotation
	
	// reusable pointcut expression uses the @Pointcut annotation
	// over a hook method (an empty method that you use to put an annotation
	@Pointcut("execution(* com.revature..* (..))")
	public void targetedJoinpoints() { } // this is a called a hook
	
	// 5 types of advice: determines when the advice runs in relation to the targeted method
	// @Before - run before the method
	// @After - run after the method
	// @AfterReturning - run after the method ONLY if no exceptions were thrown
	// @AfterThrowing - run after the method ONLY if an exception/error was thrown
	// @Around - runs before the method, decides when the method runs, runs after the method
	
	// we specify the hook here to use the reusable pointcut we created
	// this advice will target any methods targeted by that pointcut
//	@Before("targetedJoinpoints()")
//	public void beforeMethods(JoinPoint targetedMethod) {
//		System.out.println("BEFORE ADVICE: " + targetedMethod.getSignature());
//	}
//	
//	@After("targetedJoinpoints()")
//	public void afterMethods() {
//		System.out.println("AFTER ADVICE");
//	}
//	
//	@AfterReturning(pointcut="targetedJoinpoints()", returning="returnedObj")
//	public void afterReturning(Object returnedObj) {
//		System.out.println("AFTER RETURNING (SUCCESS): " + returnedObj);
//	}
//	
//	@AfterThrowing(pointcut="targetedJoinpoints()", throwing="thrownThing")
//	public void afterThrowing(Throwable thrownThing) {
//		System.out.println("AFTER THROWING AN EXCEPTION/ERROR: " + thrownThing.getClass());
//	}
	
	// around advice needs to return an Object and take a ProceedingJoinPoint parameter
	// and needs to throw a Throwable just in case an exception/error is thrown
//	@Around("targetedJoinpoints()")
//	public Object around(ProceedingJoinPoint targetedMethod) throws Throwable {
//		System.out.println("AROUND ADVICE");
////		String[] strings = {"hi", "hello", "this is definitely, totally a pet"};
////		return ResponseEntity.ok(strings);
//		
//		// proceed allows the targeted method to run as normal
//		Object returnedObj = targetedMethod.proceed();
//		
//		System.out.println("METHOD RETURNED: " + returnedObj);
//		
//		if (returnedObj instanceof Pet) {
//			Pet returnedPet = (Pet) returnedObj;
//			returnedPet.setName("New Fake Name");
//			returnedObj = returnedPet;
//		} else if (returnedObj instanceof Set) {
//			Set<Pet> pets = (Set) returnedObj;
//			for (Pet pet : pets) {
//				pet.setName("fake pet name");
//			}
//			return pets;
//		}
//		
//		return returnedObj;
//	}
}
