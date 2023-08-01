package org.poolc.api.mail.service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import org.poolc.api.mail.dto.MailDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String sender = "careerpoolc@gmail.com";

    @Async
    public void sendSimpleMailMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailDto.getReceiver());
        message.setFrom(sender);
        message.setSubject(mailDto.getSubject());
        message.setText(mailDto.getText());

        javaMailSender.send(message);
    }

    @Async
    public void sendMimeMessage(MailDto mailDto) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDto.getReceiver()));
            message.setFrom(new InternetAddress(sender));
            message.setSubject(mailDto.getSubject());
            message.setContent(mailDto.getText(), "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
