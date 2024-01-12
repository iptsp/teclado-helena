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


import br.ipt.thl.ioc.IocContainer;
import br.ipt.thl.keyboard.KeyboardEventType;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventDispatcher implements EventDispatcher {

    private final IocContainer iocContainer;

    public DefaultEventDispatcher(final IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    private void publish(SystemApplicationEvent<?> systemApplicationEvent) {
        this.iocContainer.publishEvent(systemApplicationEvent);
    }

    @Override
    public void keyboardInput(final String text,
                              final KeyboardEventType event) {
        this.publish(new KeyboardInputEvent(text, event));
    }

    @Override
    public void mouseRightButtonClick() {
        this.publish(new MouseRightButtonClickEvent());
    }

    @Override
    public void mouseLeftButtonClick() {
        this.publish(new MouseLeftButtonClickEvent());
    }

    @Override
    public void mouseLeftButtonPress() {
        this.publish(new MouseLeftButtonPressEvent());
    }

    @Override
    public void mouseLeftButtonRelease() {
        this.publish(new MouseLeftButtonReleaseEvent());
    }

    @Override
    public void mouseMove(int x, int y) {
        this.publish(new MouseMoveEvent(x, y));
    }

    @Override
    public void mouseScrollUp(int units) {
        this.publish(new MouseScrollUpEvent(units));
    }

    @Override
    public void mouseScrollDown(int units) {
        this.publish(new MouseScrollDownEvent(units));
    }

}
