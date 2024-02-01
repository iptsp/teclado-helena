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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para o botão esquerda do mouse.
 */
@RestController
public class MouseLeftButtonClickController {

    private final EventDispatcher eventDispatcher;

    /** Inicializador do controlador */
    @Autowired
    public MouseLeftButtonClickController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Ativa o comando de acionamento do clique.
     * @return Resposta do comando executado.
     */
    @PostMapping("/api/v1/systems/mouses/buttons/left/events/click")
    MouseLeftButtonClickControllerResponse handleKeyboardInputClick() {
        eventDispatcher.mouseLeftButtonClick();
        return new MouseLeftButtonClickControllerResponse();
    }

    /**
     * Ativa o comando de clique.
     * @return Resposta do comando executado.
     */
    @PostMapping("/api/v1/systems/mouses/buttons/left/events/press")
    MouseLeftButtonPressControllerResponse handleKeyboardInputPress() {
        eventDispatcher.mouseLeftButtonPress();
        return new MouseLeftButtonPressControllerResponse();
    }

    /**
     * Ativa o comando de liberação do clique.
     * @return Resposta do comando executado.
     */
    @PostMapping("/api/v1/systems/mouses/buttons/left/events/release")
    MouseLeftButtonReleaseControllerResponse handleKeyboardInputRelease() {
        eventDispatcher.mouseLeftButtonRelease();
        return new MouseLeftButtonReleaseControllerResponse();
    }

    /** Mensagem de resposta do clique. */
    record MouseLeftButtonClickControllerResponse() {
    }

    /** Mensagem de resposta do botão acionado. */
    record MouseLeftButtonPressControllerResponse() {
    }

    /** Mensagem de resposta do botão liberado. */
    record MouseLeftButtonReleaseControllerResponse() {
    }
}
