package br.ipt.thl.event;

import javafx.scene.input.KeyCode;

public interface EventDispatcher {

    void keyboardInputEvent(final KeyCode keyCode);

    void mouseMoveEvent(final int x,
                        final int y);
}
