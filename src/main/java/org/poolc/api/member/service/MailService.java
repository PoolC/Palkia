package org.poolc.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
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

    public void sendMessageNotification(String email, LocalDateTime sentDateTime, String messageSender, String messageReceiver) throws MessagingException, IOException {
        String year = Integer.toString(sentDateTime.getYear());
        String month = Integer.toString(sentDateTime.getMonthValue());
        String day = Integer.toString(sentDateTime.getDayOfMonth());

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("poolcmail@gmail.com");
        message.setSubject("[풀씨] 쪽지가 도착했습니다!");
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        Resource resource = new ClassPathResource("src/main/resources/template/mail.html");
        String htmlText = new String(Files.readAllBytes(resource.getFile().toPath()));
        htmlText = htmlText.replace("${year}", year);
        htmlText = htmlText.replace("${month}", month);
        htmlText = htmlText.replace("${day}", day);
        htmlText = htmlText.replace("${messageReceiver}", messageReceiver);
        htmlText = htmlText.replace("${messageSender}", messageSender);


        message.setContent(htmlText, "text/html, charset=utf-8");
        mailSender.send(message);
    }
}
