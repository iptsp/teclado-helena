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