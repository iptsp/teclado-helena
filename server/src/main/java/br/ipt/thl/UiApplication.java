package br.ipt.thl;

import br.ipt.thl.container.QRCodeContainer;
import br.ipt.thl.event.StageReadyEvent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UiApplication {

    private final QRCodeContainer qrCodeContainer;

    public UiApplication(final QRCodeContainer qrCodeContainer) {
        this.qrCodeContainer = qrCodeContainer;
    }

    @EventListener
    public void onStageReady(final StageReadyEvent stageReadyEvent) {

        var screenWidth = 300d;
        var screenHeight = 380d;
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

        var scene = new Scene(
                qrCodeContainer.rootContainer(),
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