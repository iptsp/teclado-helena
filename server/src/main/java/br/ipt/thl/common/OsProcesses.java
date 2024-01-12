//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

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
