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

import br.ipt.thl.event.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.MouseInfo;

/**
 * Componente de escuta dos eventos de mouse.
 */
@Component
public class MouseListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MouseListener.class);
    private final AsyncMouseInputService asyncMouseInputService;

    /** Inicializador do componente */
    @Autowired
    public MouseListener(final AsyncMouseInputService asyncMouseInputService) {
        this.asyncMouseInputService = asyncMouseInputService;
    }

    /**
     * Acionado por evento, envia o comando de clique do botão direito do mouse ao sistema operacional.
     */
    @EventListener(MouseRightButtonClickEvent.class)
    public void mouseRightButtonClickEvent() {

        asyncMouseInputService
                .rightButtonClick()
                .exceptionally((e) -> {
                    LOGGER.error("Error send right mouse button click", e);
                    return null;
                })
                .thenAccept(res -> LOGGER.debug("Right mouse button click sent"));
    }

    /**
     * Acionado por evento, envia o comando de clique do botão esquerdo do mouse ao sistema operacional.
     */
    @EventListener(MouseLeftButtonClickEvent.class)
    public void mouseLeftButtonClickEvent() {

        asyncMouseInputService
                .leftButtonClick()
                .exceptionally((e) -> {
                    LOGGER.error("Error send left mouse button click", e);
                    return null;
                })
                .thenAccept(res -> LOGGER.debug("Left mouse button click sent"));
    }

    /**
     * Acionado por evento, envia o comando de acionar botão esquerdo do mouse ao sistema operacional.
     */
    @EventListener(MouseLeftButtonPressEvent.class)
    public void mouseLeftButtonPressEvent() {
        asyncMouseInputService
                .leftButtonPress()
                .exceptionally((e) -> {
                    LOGGER.error("Error send left mouse button press", e);
                    return null;
                })
                .thenAccept(res -> LOGGER.debug("Left mouse button press sent"));
    }

    /**
     * Acionado por evento, envia o comando de acionar botão esquerdo do mouse ao sistema operacional.
     */
    @EventListener(MouseLeftButtonReleaseEvent.class)
    public void mouseLeftButtonReleaseEvent() {
        asyncMouseInputService
                .leftButtonRelease()
                .exceptionally((e) -> {
                    LOGGER.error("Error send left mouse button release", e);
                    return null;
                })
                .thenAccept(res -> LOGGER.debug("Left mouse button release sent"));
    }

    /**
     * Acionado por evento, envia o comando de movimento do mouse ao sistema operacional.
     */
    @EventListener
    public void mouseMoveEvent(final MouseMoveEvent mouseMoveEvent) {

        var mouseMoveEventInfo = mouseMoveEvent.source();
        var x = mouseMoveEventInfo.x();
        var y = mouseMoveEventInfo.y();

        var pointerLocation = MouseInfo.getPointerInfo().getLocation();
        var initialPositionX = (int) pointerLocation.getX();
        var initialPositionY = (int) pointerLocation.getY();

        asyncMouseInputService
                .move(initialPositionX + x, initialPositionY + y)
                .exceptionally((e) -> {
                    LOGGER.error("Error send mouse move", e);
                    return null;
                })
                .thenAccept(res -> LOGGER.debug("Mouse move sent to x: {} and y: {}", x, y));
    }

    /**
     * Acionado por evento, envia o comando de rolagem de página para cima ao sistema operacional.
     */
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
                .thenAccept(res -> LOGGER.debug("Mouse scroll up sent"));
    }

    /**
     * Acionado por evento, envia o comando de rolagem de página para baixo ao sistema operacional.
     */
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
                .thenAccept(res -> LOGGER.debug("Mouse scroll down sent"));
    }

}
