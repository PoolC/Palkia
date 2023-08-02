package org.poolc.api.member.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.dto.MailDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private static final String sender = "poolcmail@gmail.com";

    public void sendEmailPasswordResetToken(String email, String resetPasswordToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom("poolc_official@naver.com");
        helper.setSubject("풀씨 홈페이지 비밀번호 재설정 안내메일입니다.");
        helper.setText("안녕하세요.\n Poolc 홈페이지 비밀번호 재설정 안내 메일입니다. \n\n" +
                "아래 링크를 눌러 비밀번호 재설정을 진행해주세요.\n" +
                "< https://poolc.org/password/reset?token=" + resetPasswordToken + " >\n" +
                "이 링크는 24시간 동안 유효합니다.\n" +
                "감사합니다.", false);
        helper.setTo(email);
        mailSender.send(message);
    }

    @Async
    public void sendMimeMessage(MailDto mailDto) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = mailSender.createMimeMessage();
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
