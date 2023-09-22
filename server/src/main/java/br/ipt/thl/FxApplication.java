package br.ipt.thl;

import br.ipt.thl.event.StageReadyEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class FxApplication extends Application {


    private ConfigurableApplicationContext configurableApplicationContext;

    public FxApplication() {

    }

    @Override
    public void init() {
        this.configurableApplicationContext = new SpringApplicationBuilder()
                .sources(Entrypoint.class)
                .headless(false)
                .build()
                .run(args());
    }

    private String[] args() {
        return Optional.ofNullable(getParameters())
                .map(javafx.application.Application.Parameters::getRaw)
                .map(args -> args.toArray(String[]::new))
                .orElse(new String[]{});
    }

    @Override
    public void start(final Stage stage) {
        this.configurableApplicationContext
                .publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        this.configurableApplicationContext.close();
        System.exit(0);
    }
}
