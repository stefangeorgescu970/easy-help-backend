package com.easyhelp.application.utils;

import com.easyhelp.application.model.users.AppPlatform;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.utils.exceptions.PushTokenUnavailableException;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class PushNotificationUtils {

    public static void sendPushNotification(Donor donor, String message) throws PushTokenUnavailableException {
        if (donor.getPushToken() == null) throw new PushTokenUnavailableException("Donor with email " + donor.getEmail() + " does not have a push notification token");

        if (donor.getAppPlatform() == AppPlatform.IOS) {

            System.out.println("Sending an iOS push notification…");

            ApnsService service = APNS.newService()
                    .withCert("src/main/resources/easyHelpLocalPush.p12", "pass")
                    .withSandboxDestination()
                    .build();
            // TODO - here check env and set correct certificate


            String payload = APNS.newPayload()
                    .alertBody(message)
                    .build();

            System.out.println("payload: " + payload);

            service.push(donor.getPushToken(), payload);

            System.out.println("The message has been hopefully sent…");
        } else {
            // TODO - here implement android push notif
        }
    }
}
