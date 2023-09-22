package br.ipt.thl.common;

import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Platforms {
    public static <T> T runOnPlatformAsFunction(final Supplier<T> supplier) {
        var countDownLatch = new java.util.concurrent.CountDownLatch(1);
        var atomicObject = new AtomicReference<T>();
        Platform.runLater(() -> {
            var result = supplier.get();
            atomicObject.set(result);
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
            return atomicObject.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
