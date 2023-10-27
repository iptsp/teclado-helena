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
                LOGGER.error("Invalid key code: {}", keyCode);
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
                LOGGER.error("Invalid key code: {}", keyCode);
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
