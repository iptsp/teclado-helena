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
 * Controlador da ação de movimento do mouse.
 */
@RestController
public class MouseMoveController {

    private final EventDispatcher eventDispatcher;

    /** Inicializa o controlador */
    @Autowired
    public MouseMoveController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Ativa o comando de movimento do mouse.
     * @param mouseMoveControllerRequest    Dados enviados pelo Teclado Helena.
     * @return                              Retorna uma mensagem para o Teclado, garantindo que
     *                                      o controlador recebeu a mensagem.
     */
    @PostMapping("/api/v1/systems/mouses/events/move")
    MouseMoveControllerResponse handleKeyboardInput(@RequestBody MouseMoveControllerRequest
                                                            mouseMoveControllerRequest) {
        var x = mouseMoveControllerRequest.x();
        var y = mouseMoveControllerRequest.y();
        eventDispatcher.mouseMove(x, y);
        return new MouseMoveControllerResponse();
    }

    /**
     * Dados enviados pelo Teclado Helena.
     * @param x     Quantidade de movimento na horizontal.
     * @param y     Quantidade de movimento na vertical.
     */
    record MouseMoveControllerRequest(int x, int y) {
    }

    /** Mensagem de resposta. */
    record MouseMoveControllerResponse() {
    }

}
