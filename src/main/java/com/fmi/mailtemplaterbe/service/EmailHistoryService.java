package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.SendEmailErrorEntity;
import com.fmi.mailtemplaterbe.domain.entity.SentEmailEntity;
import com.fmi.mailtemplaterbe.repository.SendEmailErrorRepository;
import com.fmi.mailtemplaterbe.repository.SentEmailEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final SentEmailEntityRepository sentEmailEntityRepository;
    private final SendEmailErrorRepository sendEmailErrorRepository;

    public SentEmailEntity persistSentEmail(
            String sender, String recipient, String subject, String message, boolean sentSuccessfully) {
        SentEmailEntity sentEmailEntity = SentEmailEntity.builder()
                .senderEmail(sender)
                .recipientEmail(recipient)
                .subject(subject)
                .message(message)
                .sentSuccessfully(sentSuccessfully)
                .build();

        return sentEmailEntityRepository.save(sentEmailEntity);
    }

    public SendEmailErrorEntity persistSendEmailError(
            String sender, String recipient, String subject, String message, String error) {
        SendEmailErrorEntity sendEmailErrorEntity = SendEmailErrorEntity.builder()
                .senderEmail(sender)
                .recipientEmail(recipient)
                .subject(subject)
                .message(message)
                .error(error != null ? error : "N/A")
                .build();

        return sendEmailErrorRepository.save(sendEmailErrorEntity);
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
