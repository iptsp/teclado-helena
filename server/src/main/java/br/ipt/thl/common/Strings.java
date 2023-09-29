package br.ipt.thl.common;

public class Strings {

    public static final String SPACE = " ";

    private Strings() {

    }

    public static boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isBlank(final String value) {
        return isEmpty(value) || value.isBlank();
    }
}
