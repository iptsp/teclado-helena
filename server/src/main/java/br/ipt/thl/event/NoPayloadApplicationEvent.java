package br.ipt.thl.event;

import org.springframework.context.ApplicationEvent;

public class NoPayloadApplicationEvent extends ApplicationEvent
        implements SystemApplicationEvent<Void> {

    public NoPayloadApplicationEvent() {
        super(new Object());
    }

    @Override
    public Void source() {
        return null;
    }
}
