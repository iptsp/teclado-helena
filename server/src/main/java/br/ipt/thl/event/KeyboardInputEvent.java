package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class KeyboardInputEvent extends ApplicationEvent
        implements SystemApplicationEvent<String> {

    public KeyboardInputEvent(final String text) {
        super(text);
    }

    @Override
    public String source() {
        return (String) super.getSource();
    }
}
