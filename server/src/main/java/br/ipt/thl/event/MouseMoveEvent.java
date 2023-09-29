package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class MouseMoveEvent extends ApplicationEvent
        implements SystemApplicationEvent<MouseMoveEvent.MoveMouseEventInfo> {
    public MouseMoveEvent(int x, int y) {
        super(new MoveMouseEventInfo(x, y));
    }

    @Override
    public MoveMouseEventInfo source() {
        return (MoveMouseEventInfo) super.getSource();
    }

    public record MoveMouseEventInfo(int x, int y) {
    }

}
