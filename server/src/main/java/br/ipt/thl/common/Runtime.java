package br.ipt.thl.common;

import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public record Runtime(String[] commands) {

    public RuntimeResult execute() {
        var fullCommand = String.join(Strings.SPACE, commands);
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (byteArrayOutputStream) {
            var defaultExecuteResultHandler = new DefaultExecuteResultHandler();
            var commandLine = CommandLine.parse(fullCommand);
            var defaultExecutor = new DefaultExecutor();
            defaultExecutor.setExitValue(0);
            defaultExecutor.setStreamHandler(new PumpStreamHandler(byteArrayOutputStream));
            defaultExecutor.execute(commandLine, Map.of(), defaultExecuteResultHandler);
            defaultExecuteResultHandler.waitFor();
            int exitValue = defaultExecuteResultHandler.getExitValue();
            var exception = defaultExecuteResultHandler.getException();
            var output = Optional.ofNullable(exception)
                    .map(ExecuteException::getCause)
                    .map(Throwable::getMessage)
                    .orElse(byteArrayOutputStream.toString(StandardCharsets.UTF_8));
            if (exitValue == 0) {
                return new RuntimeResult(exitValue, RuntimeResult.Type.SUCCESS, output, fullCommand);
            } else {
                return new RuntimeResult(exitValue, RuntimeResult.Type.ERROR, output, fullCommand);
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            return new RuntimeResult(1, RuntimeResult.Type.ERROR, interruptedException.getMessage(), fullCommand);
        } catch (Exception exception) {
            return new RuntimeResult(1, RuntimeResult.Type.ERROR, exception.getMessage(), fullCommand);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runtime runtime = (Runtime) o;
        return Arrays.equals(commands, runtime.commands);
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(commands);
    }

    @Override
    public String toString() {
        return "Runtime{" +
                "command=" + Arrays.toString(commands) + '}';
    }

    public record RuntimeResult(int exitValue,
                                Type type,
                                String output,
                                String command) {
        public boolean isSuccess() {
            return type.isSuccess();
        }

        public enum Type {
            ERROR, SUCCESS;

            public boolean isSuccess() {
                return this == SUCCESS;
            }

        }

    }

    public static class Builder {

        private String[] commands;

        private Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withCommand(final String... cmds) {
            commands = cmds;
            return this;
        }

        public Runtime build() {
            return new Runtime(commands);
        }
    }
}

