package org.bank.core.common;

import java.util.Base64;

public class Base64Converter {

    public static String encode(String raw) {
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public static String decode(String encode) {
        return new String(Base64.getDecoder().decode(encode));
    }
}
