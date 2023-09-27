package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class MouseScrollUpEvent extends
        ApplicationEvent implements SystemApplicationEvent<MouseScrollUpEvent.MouseScrollUpEventInfo> {
    public MouseScrollUpEvent(int units) {
        super(new MouseScrollUpEventInfo(units));
    }

    @Override
    public MouseScrollUpEventInfo source() {
        return (MouseScrollUpEventInfo) this.getSource();
    }

    public record MouseScrollUpEventInfo(int units) {
    }
}
