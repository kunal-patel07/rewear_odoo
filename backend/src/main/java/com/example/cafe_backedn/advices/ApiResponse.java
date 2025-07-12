package com.example.cafe_backedn.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private T data;
    private ApiError error;

    public ApiResponse(){
        this.timestamp=LocalDateTime.now();
    }
    public ApiResponse(T data){
        this();
        this.data=data;
    }
    public ApiResponse(ApiError error){
        this();
        this.error=error;
    }

    public ApiResponse(String localizedMessage, HttpStatus httpStatus) {
        this();
        this.error = ApiError.builder().
                status(httpStatus)
                .message(localizedMessage)
                .error(List.of(localizedMessage)).build();
    }
}
