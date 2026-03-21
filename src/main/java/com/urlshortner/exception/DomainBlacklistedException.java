package com.urlshortner.exception;

public class DomainBlacklistedException extends Exception{
    public DomainBlacklistedException(String message){
        super(message);
    }
}
