package com.sony.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse<S,E> {
    private S data;
    private E errors;
    private String message;
    private BaseMeta metadata;

    // ✅ Success response
    public static <S,E> BaseResponse<S,E> success( S data, String message, HttpStatus status) {
        return new BaseResponse<>(data, null, message, new BaseMeta(status.value()));
    }

    // ✅ Error response
    public static <S,E> BaseResponse<S,E> error( String errorMessage, HttpStatus status) {
        return new BaseResponse<>(null,null, errorMessage,  new BaseMeta(status.value()));
    }

    public static <S,E> BaseResponse<S,E> errors(E errors,  HttpStatus status) {
        return new BaseResponse<>(null,errors, "",  new BaseMeta(status.value()));
    }
}
