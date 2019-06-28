package com.easyhelp.application.utils;

import com.easyhelp.application.model.users.PartnerUser;
import com.easyhelp.application.utils.exceptions.EasyHelpException;
import org.springframework.scheduling.annotation.Async;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    private static MimeMessage message() {
        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        //Establishing a session with required user details
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("easyhelp.appcontact@gmail.com", System.getenv("EMAIL_PASS"));
            }
        });

        return new MimeMessage(session);
    }

    private static void sendMailHelper(String subject, String body, String to) throws EasyHelpException {

        try {
            MimeMessage msg = message();
            InternetAddress[] address = InternetAddress.parse(to, true);
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body);
            msg.setHeader("XPriority", "1");
            Transport.send(msg);
            System.out.print("Did send email");
        } catch (MessagingException e) {
            throw new EasyHelpException("mailSendingFailed");
        } catch (Exception e) {
            throw e;
        }
    }

    @Async
    public static void sendAccountReviewedEmail(PartnerUser partnerUser, Boolean didAccept) throws EasyHelpException {
        String to = partnerUser.getEmail();
        String subject = "Easy Help - Account Reviewed - No Reply";
        String body = "Dear " + partnerUser.getFirstName() + ",\n\n";
        body += "Your account has been reviewed by one of our system admins.\n";
        body += "The current status of your account is " + (didAccept ? "active" : "banned") + ".\n\n";

        if (didAccept) {
            body += "Thank you for being a hero in the fight for human life.\n\n";
        } else {
            body += "If you have any questions, write to us at easyhelp.appcontact@gmail.com.\n\n";
        }

        body += "Kindest regards,\n";
        body += "the Easy Help Team";

        sendMailHelper(subject, body, to);
    }
}

