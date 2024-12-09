package com.example.skills_project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValueOfEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "O valor deve ser um dos valores permitidos.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
