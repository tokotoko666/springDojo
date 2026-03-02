package com.example.blog.web.advise;

import com.example.blog.model.*;
import com.example.blog.service.exception.UnAuthorizedResourceAccessException;
import com.example.blog.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerAdvise {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequest> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        var body = new BadRequest();
        BeanUtils.copyProperties(e.getBody(), body);
        body.setInstance(URI.create(request.getRequestURI()));

        var locale = LocaleContextHolder.getLocale();
        var errorDetailList = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> {
                    var pointer = "#/" + fieldError.getField();
                    var detail = messageSource.getMessage(fieldError, locale);
                    var errorDetail = new ErrorDetail();
                    errorDetail.setPointer(pointer);
                    errorDetail.setDetail(detail);
                    return errorDetail;
                }).toList();

        body.setErrors(errorDetailList);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InternalServerError> handleInternalServerError(RuntimeException e, HttpServletRequest request) {
        log.error("An unexpected error occurred. Returning InternalServerError to client.", e);
        return ResponseEntity
                .internalServerError()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(new InternalServerError().instance(URI.create(request.getRequestURI())));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<NotFound> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(new NotFound().instance(URI.create(request.getRequestURI())));
    }

    @ExceptionHandler(UnAuthorizedResourceAccessException.class)
    public ResponseEntity<Forbidden> handleUnAuthorizedResourceAccessException(UnAuthorizedResourceAccessException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(new Forbidden()
                        .detail("リソースへのアクセスが拒否されました")
                        .instance(URI.create(request.getRequestURI()))
                );
    }
}
