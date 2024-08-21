package com.dhan.pdf_generator.java.model;

import org.json.JSONObject;


public class Request {

    private String request_type;

    private JSONObject request_body;


    @Override
    public String toString() {
        return "Request{" +
                "request_type='" + request_type + '\'' +
                ", request_body=" + request_body +
                '}';
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public JSONObject getRequest_body() {
        return request_body;
    }

    public void setRequest_body(JSONObject request_body) {
        this.request_body = request_body;
    }
}
