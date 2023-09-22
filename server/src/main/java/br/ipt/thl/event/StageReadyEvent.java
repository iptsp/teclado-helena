
package br.ipt.thl.event;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class StageReadyEvent extends ApplicationEvent
        implements SystemApplicationEvent<Stage> {

    public StageReadyEvent(final Stage stage) {
        super(stage);
    }

    @Override
    public Stage source() {
        return (Stage) getSource();
    }
}
