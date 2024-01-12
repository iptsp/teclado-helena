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
