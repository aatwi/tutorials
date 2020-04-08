package dev.aatwi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreeterTest {

    @Test
    public void
    it_should_return_a_greeting_message() {
        Greeter mark = Greeter.aGreeter("Mark");
        assertEquals(" John, welcome to our shop, my name is Mark.",
                mark.greet("John"));
    }
}
