package br.ipt.thl.os;

import br.ipt.thl.common.Platforms;
import br.ipt.thl.executors.ExecutorsConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

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
    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> keyPress(int keyCode) {
        return runOnPlatformAsFunction(() -> {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return null;
        });
    }
}
