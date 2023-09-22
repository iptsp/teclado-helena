package br.ipt.thl.event;

import javafx.scene.input.KeyCode;
import org.springframework.context.ApplicationEvent;

public class KeyboardInputEvent extends ApplicationEvent
        implements SystemApplicationEvent<KeyCode> {

    public KeyboardInputEvent(final KeyCode keyCode) {
        super(keyCode);
    }

    @Override
    public KeyCode source() {
        return (KeyCode) super.getSource();
    }
}
