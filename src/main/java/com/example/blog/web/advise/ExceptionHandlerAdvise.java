package com.example.blog.web.advise;

import com.example.blog.model.BadRequest;
import com.example.blog.model.ErrorDetail;
import com.example.blog.model.InternalServerError;
import com.example.blog.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvise {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequest> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var body = new BadRequest();
        BeanUtils.copyProperties(e.getBody(), body);

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
    public ResponseEntity<InternalServerError> handleInternalServerError(RuntimeException e) {
        return ResponseEntity
                .internalServerError()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(new InternalServerError());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
