package br.ipt.thl.keyboard;

import br.ipt.thl.event.KeyboardInputEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class KeyboardInputListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(KeyboardInputListener.class);
    private final AsyncKeyboardInputService asyncKeyboardInputService;

    @Autowired
    public KeyboardInputListener(final AsyncKeyboardInputService asyncKeyboardInputService) {
        this.asyncKeyboardInputService = asyncKeyboardInputService;
    }

    @EventListener
    public void keyboardInputEvent(final KeyboardInputEvent keyboardInputEvent) {
        var inputEvent = keyboardInputEvent.source();
        asyncKeyboardInputService
                .sendText(inputEvent.text(), inputEvent.event())
                .exceptionally((e) -> {
                    LOGGER.error("Error sending keyboard input: {}, {}", inputEvent.text(), inputEvent.event(), e);
                    return null;
                })
                .thenAccept((res) -> {
                    LOGGER.debug("Keyboard input sent: {}, {}", inputEvent.text(), inputEvent.event());
                });
    }

}
