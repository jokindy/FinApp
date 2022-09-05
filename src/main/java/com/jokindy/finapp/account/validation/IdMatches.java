package com.jokindy.finapp.account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IdMatchesValidator.class)
@Documented
public @interface IdMatches {
    String message() default "Id's match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
