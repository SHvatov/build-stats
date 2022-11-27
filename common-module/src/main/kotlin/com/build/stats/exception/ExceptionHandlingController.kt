package com.build.stats.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ExceptionHandlingController : ResponseEntityExceptionHandler() {
    // todo: in conclusion, analyze what exceptions are used in the project and how we should response
    @ExceptionHandler(value = [Exception::class, IllegalStateException::class])
    fun handleBasicException(exception: Exception, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
            exception, null, HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR, request
        )
    }
}