package dev.aatwi;

public final class Greeter {

    private static final String DEFAULT_MESSAGE = "Hello ${GUEST_NAME}, welcome to our shop, my name is ${GREETER_NAME}.";
    private String greeterName;

    private Greeter(String greeterName) {
        this.greeterName = greeterName;
    }

    public static Greeter aGreeter(String greeterName) {
        return new Greeter(greeterName);
    }

    public String greet(String guestName) {
        return DEFAULT_MESSAGE.replace("${GUEST_NAME}", guestName)
                .replace("${GREETER_NAME}", this.greeterName);
    }
}
