package br.ipt.thl.os;

import br.ipt.thl.common.Platforms;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.Callable;

@Component
public class WindowsOsDispatcher implements OsDispatcher {

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
            robot.keyPress(keyCode);
            return null;
        });
    }

    @Override
    public void keyRelease(final int keyCode) {
        runOnPlatformAsFunction(() -> {
            robot.keyRelease(keyCode);
            return null;
        });
    }
}
