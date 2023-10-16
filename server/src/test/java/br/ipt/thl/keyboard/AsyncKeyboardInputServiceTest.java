package br.ipt.thl.keyboard;

import br.ipt.thl.junit.AbstractIntegrationTest;
import br.ipt.thl.os.OsDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.awt.event.KeyEvent;

class AsyncKeyboardInputServiceTest extends AbstractIntegrationTest {


    @Autowired
    private AsyncKeyboardInputService asyncKeyboardInputService;

    @MockBean
    private OsDispatcher osDispatcher;

    @Test
    void checkSingleLetterInLowerCase() {
        asyncKeyboardInputService.sendText("a", "released");
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(1))
                .keyPress(KeyEvent.VK_A);
        inOrder.verify(osDispatcher, times(1))
                .keyRelease(KeyEvent.VK_A);
    }

    @Test
    void checkSingleLetterInUpperCase() {
        asyncKeyboardInputService.sendText("A", "released");
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(2))
                .keyPress(anyInt());
        inOrder.verify(osDispatcher, times(2))
                .keyRelease(anyInt());
    }

    @Test
    void checkSingleLetterWithAccent() {
        asyncKeyboardInputService.sendText("á", "released");
        verify(osDispatcher, times(5))
                .keyPress(anyInt());
        verify(osDispatcher, times(5))
                .keyRelease(anyInt());
    }
}