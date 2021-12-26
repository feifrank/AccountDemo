package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String idenityId;

    public ResourceNotFoundException(String idenityId) {
        this.idenityId = idenityId;
    }

    public String getIdenityId() {
        return idenityId;
    }
}