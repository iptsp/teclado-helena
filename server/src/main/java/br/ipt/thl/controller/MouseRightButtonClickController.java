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
 * Controlador para o botão direito do mouse.
 */
@RestController
public class MouseRightButtonClickController {

    private final EventDispatcher eventDispatcher;

    /** Inicialização do controlador */
    @Autowired
    public MouseRightButtonClickController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Ativa o comando de acionamento do clique.
     * @return Resposta do comando executado.
     */
    @PostMapping("/api/v1/systems/mouses/buttons/right/events/click")
    MouseRightButtonClickControllerResponse handleKeyboardInput() {
        eventDispatcher.mouseRightButtonClick();
        return new MouseRightButtonClickControllerResponse();
    }

    /** Mensagem de resposta do botão liberado. */
    record MouseRightButtonClickControllerResponse() {
    }
}
