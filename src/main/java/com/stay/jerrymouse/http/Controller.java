package com.stay.jerrymouse.http;

import java.io.IOException;
import java.util.Stack;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public abstract class Controller {
    public void doGet(Request request,Response response) throws IOException {
        response.setStatus(Status.METHOD_NOT_ALLOWED);
        response.println(Status.METHOD_NOT_ALLOWED.getReason());
    }

    public void doPost(Request request,Response response) throws IOException {
        response.setStatus(Status.METHOD_NOT_ALLOWED);
        response.println(Status.METHOD_NOT_ALLOWED.getReason());
    }
}
