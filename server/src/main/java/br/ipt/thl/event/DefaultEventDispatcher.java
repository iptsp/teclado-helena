package br.ipt.thl.event;


import br.ipt.thl.ioc.IocContainer;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventDispatcher implements EventDispatcher {

    private final IocContainer iocContainer;

    public DefaultEventDispatcher(final IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    private void publish(SystemApplicationEvent<?> systemApplicationEvent) {
        this.iocContainer.publishEvent(systemApplicationEvent);
    }

    @Override
    public void keyboardInputEvent(final KeyCode keyCode) {
        this.publish(new KeyboardInputEvent(keyCode));
    }

    @Override
    public void mouseMoveEvent(final int x,
                               final int y) {
        this.iocContainer.publishEvent(new MouseMoveEvent(x, y));
    }
}
