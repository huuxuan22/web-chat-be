package com.example.webchat.validation.chat_validation;

import com.example.webchat.validation.ExistUserByFullnameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExistChatValidator.class)
public @interface NotExistChat {
    String message() default "*Đoạn chat này không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
