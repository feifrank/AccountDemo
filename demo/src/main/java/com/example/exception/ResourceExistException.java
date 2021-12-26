package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceExistException extends RuntimeException{

    private String idenityId;
    public ResourceExistException(String idenityId) {
        this.idenityId = idenityId;
    }

    public String getIdenityId() {
        return idenityId;
    }
}