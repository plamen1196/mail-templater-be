package com.fmi.mailtemplaterbe.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public final class EmailMessageUtil {

    private EmailMessageUtil() {

    }

    /**
     * Builds an email message based on an email template and placeholders.
     * The result email message is an implementation of email template, where all placeholders are replaced
     * with their respective values from the provided placeholders parameter.
     * <p></p>
     * Example:
     *  <p></p>
     * emailTemplate: "Hello %(name), come to my party on %(day)!" and
     *  <p></p>
     * placeholders: { name -> john, day -> friday }
     *   <p></p>
     * will produce "Hello john, come to my party on friday!"
     *
     * @param emailTemplate Email template
     * @param placeholders  Map with placeholders and their respective values
     * @return Ready to use email message with replaced placeholders
     */
    public static String buildEmailMessage(String emailTemplate, Map<String, String> placeholders) {
        final StringSubstitutor sub = new StringSubstitutor(placeholders, "%(", ")");
        final String result = sub.replace(emailTemplate);

        return result;
    }
}
