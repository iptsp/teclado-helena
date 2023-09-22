package br.ipt.thl.keys;

import br.ipt.thl.common.Platforms;
import br.ipt.thl.executors.ExecutorsConfig;
import javafx.scene.input.KeyCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class AsyncKeyboardInputService {

    private Robot robot;

    @Async(ExecutorsConfig.SEND_KEYS)
    public CompletableFuture<Boolean> send(final KeyCode keyCode) {
        var result = Platforms.runOnPlatformAsFunction(() -> {
            robot = Optional.ofNullable(robot)
                    .orElseGet(newRobot());
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

    private void pressUnicode(final Robot robot, int keyCode) {
        robot.keyPress(KeyEvent.VK_ALT);
        for (int i = 3; i >= 0; --i) {
            var newKeyCode = keyCode / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;
            robot.keyPress(newKeyCode);
            robot.keyRelease(newKeyCode);
        }
        robot.keyRelease(KeyEvent.VK_ALT);
    }
}
