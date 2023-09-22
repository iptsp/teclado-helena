package br.ipt.thl.keys;

import br.ipt.thl.common.Platforms;
import br.ipt.thl.executors.ExecutorsConfig;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncKeyboardInputService {

    private Robot robot;

    public AsyncKeyboardInputService() {
    }

    @Async(ExecutorsConfig.SEND_KEYS)
    public CompletableFuture<Boolean> send(final KeyCode keyCode) {
        var result = Platforms.runOnPlatformAsFunction(() -> {
            this.robot = Optional.ofNullable(this.robot)
                    .orElseGet(Robot::new);
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return true;
        });
        return CompletableFuture.completedFuture(result);
    }
}
