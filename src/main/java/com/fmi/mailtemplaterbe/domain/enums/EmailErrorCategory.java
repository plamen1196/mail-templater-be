package com.fmi.mailtemplaterbe.domain.enums;

/**
 * Represents the email error categories from the email_errors_categories table.
 * The values of each category must match its DB record counterpart.
 */
public enum EmailErrorCategory {
    MESSAGING(1L),
    RUNTIME(2L),
    UNKNOWN(3L);

    private Long value;

    EmailErrorCategory(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public EmailErrorCategory fromValue(Long value) {
        for (EmailErrorCategory emailErrorCategory : EmailErrorCategory.values()) {
            if (emailErrorCategory.getValue() == value) {
                return emailErrorCategory;
            }
        }

        return null;
    }
}
