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

package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

/**
 * Objeto de apoio para o evento de movimento do mouse.
 */
public class MouseMoveEvent extends ApplicationEvent
        implements SystemApplicationEvent<MouseMoveEvent.MoveMouseEventInfo> {
    /**
     * Inicializador do controlador.
     * @param x     Quantidade de movimento na horizontal.
     * @param y     Quantidade de movimento na vertical.
     */
    public MouseMoveEvent(int x, int y) {
        super(new MoveMouseEventInfo(x, y));
    }

    @Override
    public MoveMouseEventInfo source() {
        return (MoveMouseEventInfo) super.getSource();
    }

    /**
     * Dados enviados pelo Teclado Helena.
     * @param x     Quantidade de movimento na horizontal.
     * @param y     Quantidade de movimento na vertical.
     */
    public record MoveMouseEventInfo(int x, int y) {
    }

}
