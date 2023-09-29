package br.ipt.thl.keyboard;

import br.ipt.thl.junit.AbstractIntegrationTest;
import br.ipt.thl.os.OsDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class AsyncKeyboardInputServiceTest extends AbstractIntegrationTest {


    @Autowired
    private AsyncKeyboardInputService asyncKeyboardInputService;

    @MockBean
    private OsDispatcher osDispatcher;

    @Test
    void checkSingleLetterInLowerCase() {
        var result = asyncKeyboardInputService.sendText("a")
                .join();
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(1))
                .keyPress(anyInt());
        inOrder.verify(osDispatcher, times(1))
                .keyRelease(anyInt());
        assertEquals("a", result);
    }

    @Test
    void checkSingleLetterInUpperCase() {
        var result = asyncKeyboardInputService.sendText("A")
                .join();
        var inOrder = inOrder(osDispatcher);
        inOrder.verify(osDispatcher, times(2))
                .keyPress(anyInt());
        inOrder.verify(osDispatcher, times(2))
                .keyRelease(anyInt());
        assertEquals("A", result);
    }

    @Test
    void checkSingleLetterWithAccent() {
        var result = asyncKeyboardInputService.sendText("á")
                .join();
        verify(osDispatcher, times(5))
                .keyPress(anyInt());
        verify(osDispatcher, times(5))
                .keyRelease(anyInt());
        assertEquals("á", result);
    }
}