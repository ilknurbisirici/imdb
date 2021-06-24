package com.vitelco.imdb.exceptions;


import lombok.Getter;
import lombok.ToString;

@ToString
public class InvalidArgumentException extends RuntimeException {

    private static final long serialVersionUID = -3889348172078642759L;
    @Getter
    private String  message;
    @Getter
    private String details;
    @Getter
    private Integer status;

    public InvalidArgumentException(String message) {
        super(message);
        this.message = message;
        this.setStackTrace(new StackTraceElement[0]);
    }
    public InvalidArgumentException(String message, String details,Integer status ) {
        super(details);
        this.status = status;
        this.details = details;
        this.message = message;
        this.setStackTrace(new StackTraceElement[0]);
    }
}

