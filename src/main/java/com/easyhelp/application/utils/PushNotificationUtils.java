package com.easyhelp.application.utils;

import com.easyhelp.application.model.users.AppPlatform;
import com.easyhelp.application.model.users.Donor;
import com.easyhelp.application.service.android_pn_service.AndroidPushNotificationsService;
import com.easyhelp.application.utils.exceptions.PushTokenUnavailableException;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PushNotificationUtils {

    private static AndroidPushNotificationsService androidPushNotificationsService = new AndroidPushNotificationsService();

    public static void sendPushNotification(Donor donor, String message) throws PushTokenUnavailableException {
        if (donor.getPushToken() == null)
            throw new PushTokenUnavailableException("Donor with email " + donor.getEmail() + " does not have a push notification token");

        if (donor.getAppPlatform() == AppPlatform.IOS) {

            System.out.println("Sending an iOS push notification…");

            ApnsService service = APNS.newService()
                    .withCert("src/main/resources/easyHelpLocalPush.p12", "pass")
                    .withSandboxDestination()
                    .build();
            // TODO - here check env and set correct certificate

            String payload = APNS.newPayload()
                    .alertBody(message)
                    .sound("default")
                    .build();

            System.out.println("payload: " + payload);

            service.push(donor.getPushToken(), payload);

            System.out.println("The message has been hopefully sent…");
        } else if (donor.getAppPlatform() == AppPlatform.ANDROID) {
            try {
                JSONObject body = new JSONObject();
                body.put("priority", "high");

                JSONObject notification = new JSONObject();
                notification.put("title", "Easy Help");
                notification.put("body", message);

                body.put("notification", notification);

                body.put("to", donor.getPushToken());

                HttpEntity<String> request = new HttpEntity<>(body.toString());

                CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
                CompletableFuture.allOf(pushNotification).join();


                String firebaseResponse = pushNotification.get();
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
