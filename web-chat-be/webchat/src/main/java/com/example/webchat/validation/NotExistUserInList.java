package com.example.webchat.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotExistUsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotExistUserInList {
    String message() default "Danh sách user chứa ID không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
