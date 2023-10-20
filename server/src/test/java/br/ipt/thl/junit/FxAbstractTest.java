package br.ipt.thl.junit;


import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class FxAbstractTest extends AbstractTest {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final AtomicInteger counter = new AtomicInteger(0);

    @BeforeAll
    static void beforeAll() throws InterruptedException {
        var currentCount = counter.getAndIncrement();
        if (currentCount == 0) {
            var countDownLatch = new CountDownLatch(1);
            executorService.submit(() -> Platform.startup(countDownLatch::countDown));
            countDownLatch.await();
        }
    }

    @AfterAll
    static void afterAll() {
        if (counter.get() == 0) {
            Platform.exit();
            executorService.shutdownNow();
        }
    }

}