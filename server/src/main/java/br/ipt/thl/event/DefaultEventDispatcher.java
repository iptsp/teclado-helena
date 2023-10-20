package br.ipt.thl.event;


import br.ipt.thl.ioc.IocContainer;
import br.ipt.thl.keyboard.KeyboardEventType;
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
    public void keyboardInput(final String text,
                              final KeyboardEventType event) {
        this.publish(new KeyboardInputEvent(text, event));
    }

    @Override
    public void mouseRightButtonClick() {
        this.publish(new MouseRightButtonClickEvent());
    }

    @Override
    public void mouseLeftButtonClick() {
        this.publish(new MouseLeftButtonClickEvent());
    }

    @Override
    public void mouseMove(int x, int y) {
        this.publish(new MouseMoveEvent(x, y));
    }

    @Override
    public void mouseScrollUp(int units) {
        this.publish(new MouseScrollUpEvent(units));
    }

    @Override
    public void mouseScrollDown(int units) {
        this.publish(new MouseScrollDownEvent(units));
    }

}
