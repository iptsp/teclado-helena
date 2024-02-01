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
 * Objeto de apoio para o evento de rolagem de página para baixo.
 */
public class MouseScrollDownEvent extends
        ApplicationEvent implements SystemApplicationEvent<MouseScrollDownEvent.MouseScrollDownEventInfo> {
    public MouseScrollDownEvent(int units) {
        super(new MouseScrollDownEventInfo(units));
    }

    @Override
    public MouseScrollDownEventInfo source() {
        return (MouseScrollDownEventInfo) this.getSource();
    }

    public record MouseScrollDownEventInfo(int units) {
    }
}
