package com.fyo.ggboo.infra.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Date Validation Annotation
 * 
 * @author boolancpain
 */
@Documented
@Constraint(validatedBy = DateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {
	
	String message() default "Invalid Date";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}