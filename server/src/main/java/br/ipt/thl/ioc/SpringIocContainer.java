package br.ipt.thl.ioc;

import br.ipt.thl.event.SystemApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringIocContainer implements IocContainer {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringIocContainer(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publishEvent(final SystemApplicationEvent<?> keyboardEvent) {
        applicationContext.publishEvent(keyboardEvent);
    }

}
