package com.foxminded.university.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class ValidateInputTest {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    public final PrintStream out = System.out;

    @BeforeEach
    public void loadCustomSystemOut() {
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @AfterEach
    public void loadDefaultSystemOut() {
        System.setOut(this.out);
    }

    @Test
    public void shouldReturnStringOfExceptionWhenEnterNonNumbers() {
        ValidateInput input = new ValidateInput(new SimulateInput(Arrays.asList("invalid", "1")));
        input.ask("Enter", singletonList(1));
        String actual = byteArrayOutputStream.toString();
        String expected = String.format("Please enter validate data again.%s", LINE_SEPARATOR);
        assertEquals(expected, actual,
                "letters are not allowed should return string describe of problem");
    }
    @Test
    public void shouldReturnStringOfExceptionWhenEnterWrongNumberOfMenu() {
        ValidateInput input = new ValidateInput(
                new SimulateInput(Arrays.asList("2", "1"))
        );
        input.ask("Enter", singletonList(1));
        String actual = byteArrayOutputStream.toString();
        String expected = String.format("Please select key from menu.%s", LINE_SEPARATOR);
        assertEquals(expected, actual,
                "Should return string describe problem that you input wrong menu number");
    }
}