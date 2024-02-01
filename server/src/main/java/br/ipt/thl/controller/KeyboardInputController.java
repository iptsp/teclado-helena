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

package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import br.ipt.thl.keyboard.KeyboardEventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para o acionamento do teclado.
 * Ao acionar uma tecla, o comando é enviado para este controlador.
 */
@RestController
@Validated
public class KeyboardInputController {

    private final EventDispatcher eventDispatcher;

    /** Inicializador do controlador */
    @Autowired
    public KeyboardInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Recebe os dados enviados pelo Teclado Helena e os processa.
     *
     * @param keyboardInputControllerRequest    Dados enviados pelo Teclado Helena.
     * @return                                  Retorna uma mensagem para o Teclado, garantindo que
     *                                          o controlador recebeu a mensagem.
     */
    @PostMapping("/api/v1/systems/keyboards/inputs")
    KeyboardInputControllerResponse handleKeyboardInput(@RequestBody @Valid final KeyboardInputControllerRequest
                                                                keyboardInputControllerRequest) {
        var text = keyboardInputControllerRequest.text();
        var event = keyboardInputControllerRequest.event();

        eventDispatcher.keyboardInput(text, event);

        return new KeyboardInputControllerResponse();
    }

    /** Mensagem de resposta. */
    record KeyboardInputControllerResponse() {
    }

    /**
     * Dados enviados pelo Teclado Helena.
     *
     * @param text      Caracter ou comando em formato de texto.
     * @param event     Tipo de evento realizado, ACIONADO ou LIBERADO.
     */
    record KeyboardInputControllerRequest(@NotBlank String text,
                                          @NotNull KeyboardEventType event) {
    }
}
