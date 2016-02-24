package com.journme.rest.user.service;

public interface NotificationService {

    SSEChannel registerSSEChannel(String userId, String authToken);

}
