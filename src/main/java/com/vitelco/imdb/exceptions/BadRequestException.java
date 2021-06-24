package com.vitelco.imdb.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
public class BadRequestException extends Exception {

    private static final long serialVersionUID = -3889348172078642759L;
    @Getter
    private String message;
    @Getter
    private String details;
    @Getter
    private Integer status;

    public BadRequestException(String message) {
        this.message = message;
		this.setStackTrace(new StackTraceElement[0]);
    }

    public BadRequestException(String message, String details, Integer status) {
        this.status = status;
        this.details = details;
        this.message = message;
		this.setStackTrace(new StackTraceElement[0]);
    }


}
