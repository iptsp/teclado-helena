package br.ipt.thl.mouse;

import br.ipt.thl.event.MouseMoveEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MouseInputListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MouseInputListener.class);
    private final AsyncMouseInputService asyncMouseInputService;

    @Autowired
    public MouseInputListener(final AsyncMouseInputService asyncMouseInputService) {
        this.asyncMouseInputService = asyncMouseInputService;
    }

    @EventListener
    public void keyboardInputEvent(final MouseMoveEvent mouseMoveEvent) {
        var mouseMoveEventInfo = mouseMoveEvent.source();
        var x = mouseMoveEventInfo.x();
        var y = mouseMoveEventInfo.y();
        asyncMouseInputService
                .send(x, y)
                .thenAccept(result -> {
                    LOGGER.debug("Keyboard input sent: {}", result);
                });
    }

}
