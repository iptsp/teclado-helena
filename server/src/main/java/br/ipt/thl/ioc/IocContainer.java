package br.ipt.thl.ioc;

import br.ipt.thl.event.SystemApplicationEvent;

public interface IocContainer {

    void publishEvent(final SystemApplicationEvent<?> systemApplicationEvent);

}
