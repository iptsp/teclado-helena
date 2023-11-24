package br.ipt.thl.mouse;

import br.ipt.thl.executors.ExecutorsConfig;
import br.ipt.thl.os.OsDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.event.InputEvent;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncMouseInputService {

    private final OsDispatcher osDispatcher;

    @Autowired
    public AsyncMouseInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> rightButtonClick() {
        osDispatcher.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        osDispatcher.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> leftButtonClick() {
        osDispatcher.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        osDispatcher.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> leftButtonPress() {
        osDispatcher.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> leftButtonRelease() {
        osDispatcher.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> move(final int x,
                                        final int y) {
        osDispatcher.mouseMove(x, y);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> scrollUp(final int amount) {
        osDispatcher.mouseWheel(-amount);
        return CompletableFuture.completedFuture(null);
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> scrollDown(final int amount) {
        osDispatcher.mouseWheel(amount);
        return CompletableFuture.completedFuture(null);
    }


}
