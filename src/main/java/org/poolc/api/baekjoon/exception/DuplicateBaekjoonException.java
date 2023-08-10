package org.poolc.api.baekjoon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBaekjoonException extends RuntimeException{
    public DuplicateBaekjoonException(String message){
        super(message);
    }
}
