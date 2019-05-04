package com.easyhelp.application.controller;

import com.easyhelp.application.service.android_pn_service.AndroidPushNotificationsService;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {

    private final String TOPIC = "JavaSampleApproach";

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @RequestMapping("/iOSnotification")
    public void notification() {

        System.out.println("Sending an iOS push notification…");

        ApnsService service = APNS.newService()
                .withCert("src/main/resources/easyHelpLocalPush.p12", "pass")
                .withSandboxDestination()
                .build();

        String payload = APNS.newPayload()
                .alertBody("Can’t be simpler than this!")
                .build();

        String token = "da44813cfaa67a63c685dbdad5cb3c2b5d87d25ccb3f681a347fdc9d26467003";

        System.out.println("payload: " + payload);

        service.push(token, payload);

        System.out.println("The message has been hopefully sent…");
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() throws JSONException {

        JSONObject body = new JSONObject();
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "JSA Notification");
        notification.put("body", "CE ZEU");

        JSONObject data = new JSONObject();
        data.put("Key-1", "JSA Data 1");
        data.put("Key-2", "JSA Data 2");

        body.put("notification", notification);
        body.put("data", data);
        body.put("to", "cSuLZ9iRJTA:APA91bH5ECYflHXKjmxT9z0BrplZ1FcWaxFfxr_1yKfxsqX0XLN1Up_YMXTVU8vsPpCS0FftptEaYkp3_P8yrakLEakbfseXR_VHIZyrlFQh2dkGFLCQJxzp2rG4nvFLbmIgAvILG_7l");


        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}

