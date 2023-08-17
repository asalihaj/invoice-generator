package com.techframe.invoicegenerator.util;

public class IdGenerator {
    public static String generateId(String prefix, int idLength, int counter) {
        String idNumber = String.format("%0" + idLength + "d", counter);
        return prefix + idNumber;
    }
}
