package com.example.cinemaCore.exceptions;

public class EmailNotFoundException extends Throwable {
    public EmailNotFoundException(String pleaseEnterAValidEmail) {
        super(pleaseEnterAValidEmail);
    }
}
