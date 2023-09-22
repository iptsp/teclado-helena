package br.ipt.th;

import br.ipt.th.fx.FxLabel;
import br.ipt.th.fx.FxStackPane;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
        var screenWidth = 100d;
        var screenHeight = 100d;

        var primaryScreenBounds = Screen.getPrimary()
                .getBounds();
        var x = primaryScreenBounds.getWidth() / 2 - screenWidth / 2;
        var y = primaryScreenBounds.getHeight() / 2 - screenHeight / 2;


        this.stage = stage;
        this.stage.setHeight(screenHeight);
        this.stage.setWidth(screenWidth);
        this.stage.setResizable(false);
        this.stage.setMaximized(false);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setX(x);
        this.stage.setY(y);

        var fxStackPane = new FxStackPane();
        var fxLabel = new FxLabel("Splash Screen");
        fxStackPane.addOnCenter(fxLabel);

        var scene = new Scene(
                fxStackPane,
                this.stage.getWidth(),
                this.stage.getHeight()
        );
        scene.getStylesheets()
                .add("css/ui.css");
        scene.setFill(Color.TRANSPARENT);

        this.stage.setScene(scene);
        this.stage.show();
    }
}
