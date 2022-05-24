package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.config.CorsConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class EmailMessageUtil {

    private final CorsConfiguration corsConfiguration;

    /**
     * Builds an email message based on an email template and placeholders.
     * The result email message is an implementation of email template, where all placeholders are replaced
     * with their respective values from the provided placeholders parameter.
     * The placeholders suffix and prefix are used to identify the placeholders that need to be replaced.
     * <p></p>
     * Example:
     *  <p></p>
     * emailTemplate: "Hello %(name), come to my party on %(day)!" and
     *  <p></p>
     * placeholders: { name -> john, day -> friday }
     *  <p></p>
     *  placeholderPrefix: "%("
     *  <p></p>
     *  placeholderSuffix: ")"
     *  <p></p>
     * will produce "Hello john, come to my party on friday!"
     *
     * @param emailTemplate Email template
     * @param placeholders  Map with placeholders and their respective values
     * @param placeholderPrefix  Placeholder prefix
     * @param placeholderSuffix  Placeholder suffix
     * @return Ready to use email message with replaced placeholders
     */
    public String buildEmailMessage(
            String emailTemplate,
            Map<String, String> placeholders,
            String placeholderPrefix,
            String placeholderSuffix) {
        final StringSubstitutor sub = new StringSubstitutor(placeholders, placeholderPrefix, placeholderSuffix);
        final String result = sub.replace(emailTemplate);

        return result;
    }

    public String appendConfirmationAppLink(
            String emailMessage, String recipientEmail, String recipientToken, boolean isHtml) {
        final StringBuilder messageBuilder = new StringBuilder();
        final String confirmationAppLink = UriComponentsBuilder
                .fromUriString(corsConfiguration.getClientFeAppAllowedOrigin())
                .queryParam("recipientEmail", recipientEmail)
                .queryParam("recipientToken", recipientToken)
                .build()
                .toUriString();
        final String lineSeparator = isHtml ? "<br>" : System.getProperty("line.separator");

        messageBuilder.append(emailMessage);
        messageBuilder.append(lineSeparator);
        messageBuilder.append(lineSeparator);
        messageBuilder.append("Please use the link below to confirm:");
        messageBuilder.append(lineSeparator);
        messageBuilder.append(confirmationAppLink);

        return messageBuilder.toString();
    }
}
