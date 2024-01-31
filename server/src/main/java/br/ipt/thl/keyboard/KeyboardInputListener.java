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

import br.ipt.thl.event.KeyboardInputEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Componente de escuta dos eventos de teclado.
 */
@Component
public class KeyboardInputListener {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(KeyboardInputListener.class);
    private final AsyncKeyboardInputService asyncKeyboardInputService;

    /** Inicializador do componente */
    @Autowired
    public KeyboardInputListener(final AsyncKeyboardInputService asyncKeyboardInputService) {
        this.asyncKeyboardInputService = asyncKeyboardInputService;
    }

    /**
     * Acionado por evento, envia o comando da tecla acionada ao sistema operacional.
     * @param keyboardInputEvent Objeto de apoio para o evento de acionamento de tecla.
     */
    @EventListener
    public void keyboardInputEvent(final KeyboardInputEvent keyboardInputEvent) {
        var keyboardInputEventInfo = keyboardInputEvent.source();
        var text = keyboardInputEventInfo.text();
        var event = keyboardInputEventInfo.event();

        asyncKeyboardInputService
                .sendText(text, event)
                .exceptionally((e) -> {
                    LOGGER.error("Error sending keyboard input: {}, {}", text, event, e);
                    return null;
                })
                .thenAccept((res) -> LOGGER.debug("Keyboard input sent: {}, {}", text, event));
    }
}