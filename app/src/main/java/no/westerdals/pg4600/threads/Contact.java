package no.westerdals.pg4600.threads;

public class Contact {
    public String name;
    public String number;

    public Contact(final String name, final String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return name + ": " + number;
    }
}
