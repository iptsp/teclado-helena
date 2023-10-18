package br.ipt.thl.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

@ActiveProfiles(value = "test")
public abstract class AbstractTest {

    private static final String EMPTY = "";
    private static final Random RANDOM = new SecureRandom();

    public String uuid() {
        return UUID.randomUUID().toString();
    }

    public String uuid(final String source) {
        return UUID.nameUUIDFromBytes(source.getBytes(StandardCharsets.UTF_8))
                .toString();
    }



    public long randomLong(final int count) {
        return Long.parseLong(random(count, 0, 0, false, true, null, RANDOM));
    }

    public int randomInt(final int count) {
        return Integer.parseInt(random(count, 0, 0, false, true, null, RANDOM));
    }

    public String randomAlphabetic(final int count) {
        return random(count, 0, 0, true, false, null, RANDOM);
    }

    public String randomAlphanumeric(final int count) {
        return random(count, 0, 0, true, true, null, RANDOM);
    }

    private String random(int count, int start, int end,
                          final boolean letters, final boolean numbers,
                          final char[] chars, final Random random) {
        if (count == 0) {
            return EMPTY;
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else {
                if (!letters && !numbers) {
                    end = Character.MAX_CODE_POINT;
                } else {
                    end = 'z' + 1;
                    start = ' ';
                }
            }
        } else {
            if (end <= start) {
                throw new IllegalArgumentException(
                        "Parameter end (" + end + ") must be greater than start (" + start + ")");
            }
        }

        final int zero_digit_ascii = 48;
        final int first_letter_ascii = 65;

        if (chars == null && (numbers && end <= zero_digit_ascii || letters && end <= first_letter_ascii)) {
            throw new IllegalArgumentException(
                    "Parameter end (" + end + ") must be greater then (" + zero_digit_ascii + ") for generating digits "
                            + "or greater then (" + first_letter_ascii + ") for generating letters.");
        }

        final StringBuilder builder = new StringBuilder(count);
        final int gap = end - start;

        while (count-- != 0) {
            int codePoint;
            if (chars == null) {
                codePoint = random.nextInt(gap) + start;

                switch (Character.getType(codePoint)) {
                    case Character.UNASSIGNED, Character.PRIVATE_USE, Character.SURROGATE -> {
                        count++;
                        continue;
                    }
                }

            } else {
                codePoint = chars[random.nextInt(gap) + start];
            }

            final int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }

            if (letters && Character.isLetter(codePoint) || numbers && Character.isDigit(codePoint)
                    || !letters && !numbers) {
                builder.appendCodePoint(codePoint);

                if (numberOfChars == 2) {
                    count--;
                }

            } else {
                count++;
            }
        }
        return builder.toString();
    }

    public <T> T mock(final Class<T> clazz) {
        return Mockito.mock(clazz);
    }

    public <T> T spy(final Class<T> clazz) {
        return Mockito.spy(clazz);
    }

    public Stubber doReturn(final Object toBeReturned) {
        return Mockito.doReturn(toBeReturned);
    }


    public InOrder inOrder(final Object... mocks) {
        return Mockito.inOrder(mocks);
    }

    public <T> T eq(final T value) {
        return Mockito.eq(value);
    }

    public <T> T verify(final T mock, final VerificationMode verificationMode) {
        return Mockito.verify(mock, verificationMode);
    }

    public <T> T verify(final T mock) {
        return Mockito.verify(mock);
    }

    public VerificationMode never() {
        return Mockito.never();
    }

    public VerificationMode atLeastOnce() {
        return Mockito.atLeastOnce();
    }

    public VerificationMode times(final int wantedNumberOfInvocations) {
        return Mockito.times(wantedNumberOfInvocations);
    }

    public Stubber doThrow(final Throwable... toBeThrown) {
        return Mockito.doThrow(toBeThrown);
    }

    public String eq(final String value) {
        return Mockito.eq(value);
    }

    public <T> T any(final Class<T> clazz) {
        return Mockito.any(clazz);
    }

    public <T> List<T> anyList() {
        return Mockito.anyList();
    }

    public <T> Set<T> anySet() {
        return Mockito.anySet();
    }

    public int anyInt() {
        return Mockito.anyInt();
    }

    public double anyDouble() {
        return Mockito.anyDouble();
    }

    public boolean anyBoolean() {
        return Mockito.anyBoolean();
    }

    public String anyString() {
        return Mockito.anyString();
    }

    public <K, V> Map<K, V> anyMap() {
        return Mockito.anyMap();
    }

    public <T> T any() {
        return Mockito.any();
    }

    public <T> OngoingStubbing<T> when(final T methodCall) {
        return Mockito.when(methodCall);
    }

    public void assertContains(final String value, final String contains) {
        Assertions.assertTrue(value.contains(contains));
    }

    public void assertTrue(final boolean condition) {
        Assertions.assertTrue(condition);
    }

    public void assertFalse(final boolean condition) {
        Assertions.assertFalse(condition);
    }

    public void assertFail() {
        Assertions.fail();
    }

    public void assertFail(final String message) {
        Assertions.fail(message);
    }

    public void assertArraysEquals(byte[] byte1, byte[] byte2) {
        Assertions.assertArrayEquals(byte1, byte2);
    }

    public void assertSuccess() {
        assertTrue(true);
    }

    public Throwable assertThrows(final Class<? extends Throwable> expectedType, Executable executable) {
        return Assertions.assertThrows(expectedType, executable);
    }

    public void assertNotNull(final Object object) {
        Assertions.assertNotNull(object);
    }

    public void assertNull(final Object object) {
        Assertions.assertNull(object);
    }

    public void assertEquals(final Integer expected, final Integer actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void assertEquals(final int expected, final int actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void assertNotEquals(final int expected, final int actual) {
        Assertions.assertNotEquals(expected, actual);
    }

    public void assertNotEquals(final Object expected, final Object actual) {
        Assertions.assertNotEquals(expected, actual);
    }

    public void assertEquals(final long expected, final Object actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void assertEquals(final long expected, final long actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void assertEquals(String expected, String actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void assertEquals(final Object expected, final Object actual) {
        Assertions.assertEquals(expected, actual);
    }

    public void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Path createNewRandomFile(final Path dir,
                                    final byte[] data,
                                    final String suffix) {
        try {
            Path targetFile = dir.resolve(UUID.randomUUID() + suffix);
            if (data != null && data.length > 0) {
                return Files.write(targetFile, data);
            }
            return targetFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path createNewRandomDir(final Path parent) {
        return createDirectories(parent.resolve(uuid()));
    }

    public Path createNewRandomFileOnTmpDir(final String suffix) {
        return createNewRandomFile(tempDir(), new byte[]{}, suffix);
    }

    public Path createNewFileWithRandomContent(final Path target) {
        byte[] data = randomAlphanumeric(10)
                .getBytes();
        try {
            return Files.write(target, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Path createNewRandomFileWithContent(final Path dir) {
        byte[] data = randomAlphanumeric(10)
                .getBytes();
        return createNewRandomFile(dir, data, ".txt");
    }

    public Path createNewRandomFileWithContentOnTmpDir() {
        byte[] data = randomAlphanumeric(10)
                .getBytes();
        return createNewRandomFile(tempDir(), data, ".txt");
    }

    public Path createNewTempFileWithClasspath(final String resource) {
        var data = readAllBytes(path(resource));
        var extension = extensionOf(resource);
        var tempDir = tempDir();
        return createNewRandomFile(tempDir, data, "." + extension);
    }

    private Path path(String resource) {
        try {
            return classpathResource(resource)
                    .getFile()
                    .toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Resource classpathResource(final String resource) {
        return new ClassPathResource(resource, classLoader());
    }

    public static ClassLoader classLoader() {
        return AbstractTest.class.getClassLoader();
    }

    private static String extensionOf(final Path path) {
        if (path == null) {
            return "";
        }
        return extensionOf(path.getFileName().toString());
    }

    private static String extensionOf(final String fileName) {

        if (isBlank(fileName)) {
            return "";
        }

        var lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf != -1) {
            return fileName.substring(lastIndexOf + 1);
        }
        return "";
    }

    private static byte[] readAllBytes(final Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    private static boolean isBlank(final String value) {
        return isEmpty(value) || value.isBlank();
    }

    private Path tempDir() {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }

    public static Path createDirectories(final Path path) {
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}