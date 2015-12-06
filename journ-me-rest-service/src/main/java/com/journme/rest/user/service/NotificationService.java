package com.journme.rest.user.service;

public interface NotificationService {

    boolean registerSSEChannel(String userId, SSEChannel sseChannel);

}
