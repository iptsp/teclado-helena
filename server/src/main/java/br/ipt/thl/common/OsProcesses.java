package br.ipt.thl.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsProcesses {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsProcesses.class);

    private OsProcesses() {

    }

    public static boolean isRunning(final String processName) {
        if (isWindows()) return isRunningForWindows(processName);
        throw new UnsupportedOperationException("Not implemented for this OS");
    }

    private static boolean isWindows() {
        return System.getProperty("os.name")
                .toLowerCase()
                .contains("windows");
    }

    private static boolean isRunningForWindows(final String processName) {
        var fullCommand = "TASKLIST /FI \"IMAGENAME eq " + processName + "\" /fi \"STATUS eq running\" /FO csv /nh";
        var runtime = Runtime.Builder.newBuilder()
                .withCommand(fullCommand.split(Strings.SPACE))
                .build();
        var runtimeResult = runtime.execute();
        if (runtimeResult.isSuccess()) return runtimeResult.output().startsWith("\"" + processName + "\"");
        LOGGER.error("Could not determine if process is already running. Exit value {}, Output {}", runtimeResult.exitValue()
                , runtimeResult.output());
        return false;
    }
}
