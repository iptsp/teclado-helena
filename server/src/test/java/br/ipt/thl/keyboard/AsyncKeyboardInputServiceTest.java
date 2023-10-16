package br.ipt.thl.keyboard;

import br.ipt.thl.junit.AbstractIntegrationTest;
import br.ipt.thl.os.OsDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.awt.event.KeyEvent;

class AsyncKeyboardInputServiceTest extends AbstractIntegrationTest {

    private static final String PRESSED = "pressed";
    private static final String RELEASED = "released";

    @Autowired
    private AsyncKeyboardInputService asyncKeyboardInputService;

    @MockBean
    private OsDispatcher osDispatcher;

    @Test
    void checkSingleLetterInLowerCase() {
        asyncKeyboardInputService.sendText("a", PRESSED);
        asyncKeyboardInputService.sendText("a", RELEASED);
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(1))
                .keyPress(KeyEvent.VK_A);
        inOrder.verify(osDispatcher, times(1))
                .keyRelease(KeyEvent.VK_A);
    }

    @Test
    void checkSingleLetterInUpperCase() {
        asyncKeyboardInputService.sendText("shift", PRESSED);
        asyncKeyboardInputService.sendText("a", PRESSED);
        asyncKeyboardInputService.sendText("a", RELEASED);
        asyncKeyboardInputService.sendText("shift", RELEASED);
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(2))
                .keyPress(anyInt());
        inOrder.verify(osDispatcher, times(2))
                .keyRelease(anyInt());
    }

    @Test
    void checkSingleLetterWithAccent() {
        asyncKeyboardInputService.sendText("´", PRESSED);
        asyncKeyboardInputService.sendText("´", RELEASED);
        asyncKeyboardInputService.sendText("a", PRESSED);
        asyncKeyboardInputService.sendText("a", RELEASED);
        verify(osDispatcher, times(2))
                .keyPress(anyInt());
        verify(osDispatcher, times(2))
                .keyRelease(anyInt());
    }
}