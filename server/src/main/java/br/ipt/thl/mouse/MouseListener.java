package br.ipt.thl.mouse;

import br.ipt.thl.event.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MouseListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MouseListener.class);
    private final AsyncMouseInputService asyncMouseInputService;

    @Autowired
    public MouseListener(final AsyncMouseInputService asyncMouseInputService) {
        this.asyncMouseInputService = asyncMouseInputService;
    }

    @EventListener(MouseRightButtonClickEvent.class)
    public void mouseRightButtonClickEvent() {

        asyncMouseInputService
                .rightButtonClick()
                .exceptionally((e) -> {
                    LOGGER.error("Error send right mouse button click", e);
                    return null;
                })
                .thenAccept(res -> {
                    LOGGER.debug("Right mouse button click sent");
                });
    }

    @EventListener(MouseLeftButtonClickEvent.class)
    public void mouseLeftButtonClickEvent() {

        asyncMouseInputService
                .leftButtonClick()
                .exceptionally((e) -> {
                    LOGGER.error("Error send left mouse button click", e);
                    return null;
                })
                .thenAccept(res -> {
                    LOGGER.debug("Left mouse button click sent");
                });
    }

    @EventListener
    public void mouseMoveEvent(final MouseMoveEvent mouseMoveEvent) {

        var mouseMoveEventInfo = mouseMoveEvent.source();
        var x = mouseMoveEventInfo.x();
        var y = mouseMoveEventInfo.y();

        asyncMouseInputService
                .move(x, y)
                .exceptionally((e) -> {
                    LOGGER.error("Error send mouse move", e);
                    return null;
                })
                .thenAccept(res -> {
                    LOGGER.debug("Mouse move sent");
                });
    }

    @EventListener
    public void mouseScrollUpEvent(final MouseScrollUpEvent mouseScrollUpEvent) {

        var mouseScrollUpEventInfo = mouseScrollUpEvent.source();
        var units = mouseScrollUpEventInfo.units();

        asyncMouseInputService
                .scrollUp(units)
                .exceptionally((e) -> {
                    LOGGER.error("Error send mouse scroll up", e);
                    return null;
                })
                .thenAccept(res -> {
                    LOGGER.debug("Mouse scroll up sent");
                });
    }

    @EventListener
    public void mouseScrollDownEvent(final MouseScrollDownEvent mouseScrollDownEvent) {

        var mouseScrollDownEventInfo = mouseScrollDownEvent.source();
        var units = mouseScrollDownEventInfo.units();

        asyncMouseInputService
                .scrollDown(units)
                .exceptionally((e) -> {
                    LOGGER.error("Error send mouse scroll down", e);
                    return null;
                })
                .thenAccept(res -> {
                    LOGGER.debug("Mouse scroll down sent");
                });
    }

}
