package com.ecommerce.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    record ErrorBody(Instant timestamp,int status,String error,String message,String path){}
    @ExceptionHandler(ApiExceptions.UserNotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND) ErrorBody notFound(RuntimeException e,HttpServletRequest r){return body(404,"USER_NOT_FOUND",e,r);}
    @ExceptionHandler(ApiExceptions.AddressNotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND) ErrorBody addressNotFound(RuntimeException e,HttpServletRequest r){return body(404,"ADDRESS_NOT_FOUND",e,r);}
    @ExceptionHandler(ApiExceptions.DuplicateEmailException.class) @ResponseStatus(HttpStatus.CONFLICT) ErrorBody duplicateEmail(RuntimeException e,HttpServletRequest r){return body(409,"DUPLICATE_EMAIL",e,r);}
    @ExceptionHandler(ApiExceptions.DuplicateMobileException.class) @ResponseStatus(HttpStatus.CONFLICT) ErrorBody duplicateMobile(RuntimeException e,HttpServletRequest r){return body(409,"DUPLICATE_MOBILE",e,r);}
    @ExceptionHandler(ApiExceptions.InvalidCredentialsException.class) @ResponseStatus(HttpStatus.UNAUTHORIZED) ErrorBody invalid(RuntimeException e,HttpServletRequest r){return body(401,"INVALID_CREDENTIALS",e,r);}
    @ExceptionHandler(ApiExceptions.UserBlockedException.class) @ResponseStatus(HttpStatus.FORBIDDEN) ErrorBody blocked(RuntimeException e,HttpServletRequest r){return body(403,"USER_BLOCKED",e,r);}
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class) @ResponseStatus(HttpStatus.FORBIDDEN) ErrorBody denied(RuntimeException e,HttpServletRequest r){return body(403,"ACCESS_DENIED",e,r);}
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST) ErrorBody validation(MethodArgumentNotValidException e,HttpServletRequest r){String msg=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining("; "));return new ErrorBody(Instant.now(),400,"VALIDATION_ERROR",msg,r.getRequestURI());}
    @ExceptionHandler(Exception.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) ErrorBody generic(Exception e,HttpServletRequest r){return body(500,"INTERNAL_ERROR","Unexpected server error",r);}
    private ErrorBody body(int s,String code,Object m,HttpServletRequest r){return new ErrorBody(Instant.now(),s,code,String.valueOf(m),r.getRequestURI());}
}
