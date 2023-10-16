package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class KeyboardInputEvent extends ApplicationEvent
        implements SystemApplicationEvent<KeyboardInputEvent.KeyboardInputEventInfo> {

    public KeyboardInputEvent(final String text, final String event) {
        super(new KeyboardInputEventInfo(text, event));
    }

    @Override
    public KeyboardInputEvent.KeyboardInputEventInfo source() {
        return (KeyboardInputEventInfo) getSource();
    }

    public record KeyboardInputEventInfo(String text, String event) {

    }
}
