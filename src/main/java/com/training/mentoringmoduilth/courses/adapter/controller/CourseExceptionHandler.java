package com.training.mentoringmoduilth.courses.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice//bounded context exception kezelője
public class CourseExceptionHandler {
    @ExceptionHandler
    public ProblemDetail handleException(IllegalArgumentException e){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()), e.getMessage());
        problemDetail.setProperty("error-id", UUID.randomUUID().toString());
        return problemDetail;
    }
}
