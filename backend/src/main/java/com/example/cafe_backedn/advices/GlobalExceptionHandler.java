package com.example.cafe_backedn.advices;

import com.example.cafe_backedn.exception.ResourceNotFound;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse> ResourceNotFoundExceptionHandler(ResourceNotFound resourceNotFound){
        ApiResponse apiError = new ApiResponse(resourceNotFound.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> AuthenticationExceptionHandle(AuthenticationException authenticationException){
        ApiResponse apiError = new ApiResponse(authenticationException.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ApiResponse> BadCredentialsExceptionHandler(BadCredentialsException authenticationException){
//        ApiResponse apiError = new ApiResponse(authenticationException.getLocalizedMessage(), HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> JwtExceptionHandler(JwtException authenticationException){
        System.out.println("called jwt");
        ApiResponse apiError = new ApiResponse(authenticationException.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> validationErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation Failed")
                .error(validationErrors)
                .build();

        ApiResponse<Object> response = new ApiResponse<>(apiError);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("Authentication Failed")
                .error(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("API endpoint not found")
                .error(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.NOT_FOUND);
    }

}
