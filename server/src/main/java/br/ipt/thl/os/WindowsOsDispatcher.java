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

package br.ipt.thl.os;

import br.ipt.thl.common.Platforms;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.Callable;

@Component
public class WindowsOsDispatcher implements OsDispatcher {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsOsDispatcher.class);

    private final Robot robot;

    public WindowsOsDispatcher() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T runOnPlatformAsFunction(final Callable<T> runnable) {
        return Platforms.runOnPlatformAsFunction(() -> {
            try {
                return runnable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void keyPress(final int keyCode) {
        runOnPlatformAsFunction(() -> {
            try {
                robot.keyPress(keyCode);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Invalid key code: {}", keyCode, e);
            }
            return null;
        });
    }

    @Override
    public void keyRelease(final int keyCode) {
        runOnPlatformAsFunction(() -> {
            try {
                robot.keyRelease(keyCode);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Invalid key code: {}", keyCode, e);
            }
            return null;
        });
    }

    @Override
    public void mouseMove(final int x,
                          final  int y) {
        runOnPlatformAsFunction(() -> {
            robot.mouseMove(x, y);
            return null;
        });
    }

    @Override
    public void mousePress(final int buttons) {
        runOnPlatformAsFunction(() -> {
            robot.mousePress(buttons);
            return null;
        });
    }

    @Override
    public void mouseRelease(final int buttons) {
        runOnPlatformAsFunction(() -> {
            robot.mouseRelease(buttons);
            return null;
        });
    }

    @Override
    public void mouseWheel(final int wheelAmt) {
        runOnPlatformAsFunction(() -> {
            robot.mouseWheel(wheelAmt);
            return null;
        });
    }
}
