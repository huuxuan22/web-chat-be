package com.example.webchat.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistUserByFullnameValidator.class)
public @interface ExistUserByFullname {
    String message() default "*tài khoản này đã tồn taij";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
