package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;

import java.util.Comparator;

public class SentEmailsLocalDateTimeComparator implements Comparator<SentEmailResource> {

    @Override
    public int compare(SentEmailResource o1, SentEmailResource o2) {
        return o2.getTimestamp().compareTo(o1.getTimestamp());
    }
}
