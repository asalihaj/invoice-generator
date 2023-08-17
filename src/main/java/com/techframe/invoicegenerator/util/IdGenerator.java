package com.techframe.invoicegenerator.util;

public class IdGenerator {
    public static String generateUniqueId(String prefix, int idLength, int counter) {
        String idNumber = String.format("%0" + (idLength - prefix.length()) + "d", counter);
        return prefix + idNumber;    }
}
