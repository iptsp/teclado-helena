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