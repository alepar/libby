package ru.alepar.lib.stuff;

public class Oops extends RuntimeException {

    public Oops(Throwable cause) {
        super(cause);
    }

    public Oops(String message) {
        super(message);
    }
}
