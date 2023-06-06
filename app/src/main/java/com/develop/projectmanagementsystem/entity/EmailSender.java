package com.develop.projectmanagementsystem.entity;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public  class EmailSender  {

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "unnikrishnanr097@gmail.com";
    private static final String PASSWORD = "hjbrxnbhclxlcsmv";
    private static final int PORT = 587;


    public static void sendEmail(String recipient, String subject, String content) {
        final String username = "unnikrishnanr097@gmail.com";
        final String password = "jfyzbqogomksausa";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("unnikrishnanr097@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//
//            Multipart multipart = new MimeMultipart();
//
//            messageBodyPart = new MimeBodyPart();
//            String file = "path of file to be attached";
//            String fileName = "attachmentName";
//            DataSource source = new FileDataSource(file);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(fileName);
//            multipart.addBodyPart(messageBodyPart);
//            message.setContent(multipart);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        System.out.println("Done Thread");

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
