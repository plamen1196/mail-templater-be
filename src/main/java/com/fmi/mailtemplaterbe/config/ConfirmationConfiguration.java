package com.fmi.mailtemplaterbe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/* TODO: Consider usage with existing templates and FE */
@Configuration
public class ConfirmationConfiguration {

    @Value("${confirmation-reply-email.reply-message-max-length}")
    private int replyMessageMaxLength;

    public int getReplyMessageMaxLength() {
        return replyMessageMaxLength;
    }
}
