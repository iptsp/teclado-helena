package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

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
