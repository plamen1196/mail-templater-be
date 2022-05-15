package com.fmi.mailtemplaterbe.domain.enums;

/**
 * Represents the sent email confirmations from the sent_email_confirmations table.
 * The values of each confirmation must match its DB record counterpart.
 */
public enum SentEmailConfirmation {
    RECEIVED(1L),
    RECEIVED_AND_CONFIRMED(2L),
    RECEIVED_AND_REJECTED(3L);

    private Long value;

    SentEmailConfirmation(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static SentEmailConfirmation fromValue(Long value) {
        for (SentEmailConfirmation sentEmailConfirmation : SentEmailConfirmation.values()) {
            if (sentEmailConfirmation.getValue() == value) {
                return sentEmailConfirmation;
            }
        }

        return null;
    }
}
