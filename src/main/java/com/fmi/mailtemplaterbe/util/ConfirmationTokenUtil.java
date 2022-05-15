package com.fmi.mailtemplaterbe.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class ConfirmationTokenUtil {

    private static final int TOKEN_LENGTH = 64;

    private ConfirmationTokenUtil() {
    }

    public static String generateToken() {
        final String token = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);

        /* Alternative approaches */
        // RandomString.make(48);

        return token;
    }
}
