package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class MouseMoveEvent extends ApplicationEvent
        implements SystemApplicationEvent<MouseMoveEvent.MouseMoveEventInfo> {
    public MouseMoveEvent(int x, int y) {
        super(new MouseMoveEventInfo(x, y));
    }

    public MouseMoveEventInfo source() {
        return (MouseMoveEventInfo) super.getSource();
    }

    public record MouseMoveEventInfo(int x, int y) {
    }
}
