package br.ipt.thl.event;

import br.ipt.thl.keyboard.KeyboardEventType;

public interface EventDispatcher {

    void keyboardInput(final String text,
                       final KeyboardEventType event);

    void mouseRightButtonClick();

    void mouseLeftButtonClick();

    void mouseMove(int x, int y);

    void mouseScrollUp(int units);

    void mouseScrollDown(int units);
}
