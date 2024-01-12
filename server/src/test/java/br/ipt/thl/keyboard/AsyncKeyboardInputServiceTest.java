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

package br.ipt.thl.keyboard;

import br.ipt.thl.junit.FxAbstractIntegrationTest;
import br.ipt.thl.os.OsDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.awt.event.KeyEvent;

class AsyncKeyboardInputServiceTest extends FxAbstractIntegrationTest {


    @Autowired
    private AsyncKeyboardInputService asyncKeyboardInputService;

    @MockBean
    private OsDispatcher osDispatcher;

    @Test
    void checkSingleLetterInLowerCase() {
        asyncKeyboardInputService.sendText("a", KeyboardEventType.PRESSED);
        asyncKeyboardInputService.sendText("a", KeyboardEventType.RELEASED);
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(1))
                .keyPress(KeyEvent.VK_A);
        inOrder.verify(osDispatcher, times(1))
                .keyRelease(KeyEvent.VK_A);
    }

    @Test
    void checkSingleLetterInUpperCase() {
        asyncKeyboardInputService.sendText("shift", KeyboardEventType.PRESSED);
        asyncKeyboardInputService.sendText("a", KeyboardEventType.PRESSED);
        asyncKeyboardInputService.sendText("a", KeyboardEventType.RELEASED);
        asyncKeyboardInputService.sendText("shift", KeyboardEventType.RELEASED);
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(2))
                .keyPress(anyInt());
        inOrder.verify(osDispatcher, times(2))
                .keyRelease(anyInt());
    }

    @Test
    void checkSingleLetterWithAccent() {
        asyncKeyboardInputService.sendText("´", KeyboardEventType.PRESSED);
        asyncKeyboardInputService.sendText("´", KeyboardEventType.RELEASED);
        asyncKeyboardInputService.sendText("a", KeyboardEventType.PRESSED);
        asyncKeyboardInputService.sendText("a", KeyboardEventType.RELEASED);
        verify(osDispatcher, times(2))
                .keyPress(anyInt());
        verify(osDispatcher, times(2))
                .keyRelease(anyInt());
    }
}