package br.ipt.thl.event;

import br.ipt.thl.keyboard.KeyboardEventType;
import org.springframework.context.ApplicationEvent;

public class KeyboardInputEvent extends ApplicationEvent
        implements SystemApplicationEvent<KeyboardInputEvent.KeyboardInputEventInfo> {

    public KeyboardInputEvent(final String text,
                              final KeyboardEventType event) {
        super(new KeyboardInputEventInfo(text, event));
    }

    @Override
    public KeyboardInputEvent.KeyboardInputEventInfo source() {
        return (KeyboardInputEventInfo) getSource();
    }

    public record KeyboardInputEventInfo(String text, KeyboardEventType event) {

    }
}
