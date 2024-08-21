package com.example.cinemaCore.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestTest {
    private String ABC;

    public RequestTest(@JsonProperty("ABC") String ABC) {
        this.ABC = ABC;
    }

    public String getABC() {
        return ABC;
    }

    public void setABC(String ABC) {
        this.ABC = ABC;
    }
}
