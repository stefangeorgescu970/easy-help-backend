package com.easyhelp.application.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

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
}