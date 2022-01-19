package com.revature.petapp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // says that this annotation should be used on methods
@Retention(RetentionPolicy.RUNTIME) // allows us to get the info from the annotation w/ AOP
public @interface Authenticate {
	// this will be a property we can set to decide whether the user just needs to be
	// logged in or if they need to be an Employee
	String[] requiredRoles();
}
