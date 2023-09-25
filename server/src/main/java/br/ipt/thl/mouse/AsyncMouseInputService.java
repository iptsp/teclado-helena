package br.ipt.thl.mouse;

import br.ipt.thl.common.Platforms;
import br.ipt.thl.executors.ExecutorsConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class AsyncMouseInputService {

    private Robot robot;

    @Async(ExecutorsConfig.MOUSE)
    public CompletableFuture<Boolean> send(final int x,
                                           final int y) {
        var result = Platforms.runOnPlatformAsFunction(() -> {
            robot = Optional.ofNullable(robot)
                    .orElseGet(newRobot());
            robot.mouseMove(x, y);
            return true;
        });
        return CompletableFuture.completedFuture(result);
    }

    private Supplier<Robot> newRobot() {
        return () -> {
            try {
                return new Robot();
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
