package com.epam.esm.messages;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_NAME = "messages";

    public String getLocalizedMessage(Locale locale, String messageKey) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale)
                .getString(messageKey);
    }
}
