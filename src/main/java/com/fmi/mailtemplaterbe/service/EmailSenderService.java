package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.EmailConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final EmailConfiguration emailConfiguration;

    /* TODO: Implement history table and functionality */
    // @Value("${email.sent.dir}")
    // private String emailSentDir;

    public void sendEmail(String from, String to, String subject, String content, boolean isHtml) {
        try {
            Message message = new MimeMessage(emailConfiguration.createSMTPSession());
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            if (isHtml) {
                message.setContent(content, "text/html");
            } else {
                message.setText(content);
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // saveToSent(to, subject, content, false);
            throw new RuntimeException(e);
        }

        /* TODO: Save to history */
        // saveToSent(to, subject, content, true);
    }

    public String buildMail(String mailTemplate, Map<String, String> placeholders) {
        StringSubstitutor sub = new StringSubstitutor(placeholders, "%(", ")");
        String result = sub.replace(mailTemplate);

        return result;
    }

//    @Override
//    public List<EmailRecord> fetchHistory() {
//        try {
//            return Files.list(Paths.get(emailSentDir)).map(file -> {
//                        try {
//                            return TemplatesUtils.OBJECT_MAPPER
//                                    .readValue(file.toFile(), EmailRecord.class);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    })
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
