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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para o evento de rolagem de página para cima.
 */
@RestController
public class MouseScrollUpController {

    private final EventDispatcher eventDispatcher;

    /** Inicialização do controlador */
    @Autowired
    public MouseScrollUpController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Ativa o comando de acionamento da rolagem de página para cima.
     * @return Resposta do comando executado.
     */
    @PostMapping("/api/v1/systems/mouses/scrolls/events/up")
    MouseScrollUpControllerResponse handleKeyboardInput(@RequestBody MouseScrollUpControllerRequest
                                                            mouseScrollUpControllerRequest) {
        int units = mouseScrollUpControllerRequest.units();
        eventDispatcher.mouseScrollUp(units);
        return new MouseScrollUpControllerResponse();
    }

    /**
     * Dados enviados pelo Teclado Helena.
     * @param units Quantidade de rolagem de página para cima.
     */
    record MouseScrollUpControllerRequest(int units) {
    }

    /** Mensagem de resposta. */
    record MouseScrollUpControllerResponse() {
    }

}
