package com.revature.petapp.aspects;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.petapp.annotations.Authenticate;
import com.revature.petapp.beans.Person;
import com.revature.petapp.services.UserService;

@Component // marks this object as a Spring bean managed by the IOC container
@Aspect // marks this object as an Aspect class (not specific to Spring, but used by Spring AOP)
public class AuthAspect {
	// this represents the actual HTTP request coming into your controller
	// Spring can provide it to you via dependency injection
	private HttpServletRequest request;
	private UserService userServ;
	
	@Autowired
	public AuthAspect(HttpServletRequest request, UserService userServ) {
		this.request = request;
		this.userServ = userServ;
	}
	
	@Around("@annotation(com.revature.petapp.annotations.Authenticate)")
	public Object authenticate(ProceedingJoinPoint targetedMethod) throws Throwable {
		// getting the properties of the annotation on the targeted method
		Authenticate annotationProperties =
				((MethodSignature) targetedMethod.getSignature())
				.getMethod()
				.getAnnotation(Authenticate.class);
		List<String> requiredRoles = Arrays.asList(annotationProperties.requiredRoles());
		
		String token = request.getHeader("Token");
		if (token==null) {
			// this overrides the method entirely - it will return this rather
			// than even trying to run the actual controller method
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		// if the annotation's allowed roles for the method requires Employee
		if (requiredRoles.contains("Employee")) {
			Person loggedInPerson = userServ.getUserById(Integer.parseInt(token));
			// if the current logged in person's role name is NOT Employee
			if (loggedInPerson==null || !"Employee".equals(loggedInPerson.getRole().getName())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}
		
		// if they made it through all of these checks, we can proceed as normal!
		return targetedMethod.proceed();
	}
}
