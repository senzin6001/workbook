package com.example.demo.login.domain.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = {UnusedEmailValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnusedEmail {

    String message() default "このメールアドレスは既に登録されています";
    String[] applyToPages() default {};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        UnusedEmail[] value();
    }
}
