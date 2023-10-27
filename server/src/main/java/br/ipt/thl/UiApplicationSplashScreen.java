package br.ipt.thl;

import br.ipt.thl.fx.FxImageView;
import br.ipt.thl.fx.FxStackPane;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UiApplicationSplashScreen extends Preloader {

    private Stage stage;

    @Override
    public void handleStateChangeNotification(final StateChangeNotification stateChangeNotification) {
        var stateChangeNotificationType = stateChangeNotification.getType();
        if (stateChangeNotificationType == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }

    @Override
    public void start(final Stage stage) {
        var screenWidth = 500d;
        var screenHeight = 285d;

        var primaryScreenBounds = Screen.getPrimary()
                .getBounds();
        var x = primaryScreenBounds.getWidth() / 2 - screenWidth / 2;
        var y = primaryScreenBounds.getHeight() / 2 - screenHeight / 2;


        this.stage = stage;
        this.stage.setHeight(screenHeight);
        this.stage.setWidth(screenWidth);
        this.stage.setResizable(false);
        this.stage.setMaximized(false);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setX(x);
        this.stage.setY(y);

        var fxStackPane = new FxStackPane();
        var fxImageView = new FxImageView("images/splash-screen.png", 500, 285);
        fxStackPane.addOnCenter(fxImageView);
        fxStackPane.setBackground(Background.EMPTY);

        var scene = new Scene(
                fxStackPane,
                this.stage.getWidth(),
                this.stage.getHeight()
        );
        var styleSheets = scene.getStylesheets();
        styleSheets.add("css/ui.css");
        scene.setFill(Color.TRANSPARENT);

        this.stage.setScene(scene);
        this.stage.show();
    }
}