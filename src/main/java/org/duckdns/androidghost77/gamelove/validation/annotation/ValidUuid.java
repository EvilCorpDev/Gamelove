package org.duckdns.androidghost77.gamelove.validation.annotation;

import org.duckdns.androidghost77.gamelove.validation.UuidValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUuid {

    String message() default "Id is not in uuid format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
