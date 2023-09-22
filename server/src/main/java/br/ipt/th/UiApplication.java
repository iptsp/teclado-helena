package br.ipt.th;

import br.ipt.th.event.StageReadyEvent;
import br.ipt.th.fx.FxLabel;
import br.ipt.th.fx.FxStackPane;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UiApplication {

    @EventListener
    public void onStageReady(final StageReadyEvent stageReadyEvent) {

        var screenWidth = 300d;
        var screenHeight = 300d;
        var primaryScreenBounds = Screen.getPrimary()
                .getBounds();
        var x = primaryScreenBounds.getWidth() / 2 - screenWidth / 2;
        var y = primaryScreenBounds.getHeight() / 2 - screenHeight / 2;

        var stage = stageReadyEvent.source();
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        stage.setMinHeight(screenHeight);
        stage.setMinWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        stage.setResizable(false);
        stage.setX(x);
        stage.setY(y);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        var fxStackPane = new FxStackPane();
        var fxLabel = new FxLabel("QR Code");
        fxStackPane.addOnCenter(fxLabel);

        var scene = new Scene(
                fxStackPane,
                stage.getWidth(),
                stage.getHeight()
        );
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets()
                .add("css/ui.css");
        stage.setScene(scene);
        stage.show();
    }
}
