package br.ipt.thl;

import br.ipt.thl.common.Strings;
import br.ipt.thl.event.StageReadyEvent;
import br.ipt.thl.fx.FxImageView;
import br.ipt.thl.fx.FxLabel;
import br.ipt.thl.fx.FxStackPane;
import br.ipt.thl.os.NetworkInterfaceFinder;
import br.ipt.thl.qrcode.QRCodeGenerator;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UiApplication {

    private final ServletWebServerApplicationContext webServerAppContext;
    private final NetworkInterfaceFinder networkInterfaceFinder;
    private final QRCodeGenerator qrCodeGenerator;

    public UiApplication(final ServletWebServerApplicationContext webServerAppContext,
                         final NetworkInterfaceFinder networkInterfaceFinder,
                         final QRCodeGenerator qrCodeGenerator) {
        this.webServerAppContext = webServerAppContext;
        this.networkInterfaceFinder = networkInterfaceFinder;
        this.qrCodeGenerator = qrCodeGenerator;
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
                rootContainer(),
                stage.getWidth(),
                stage.getHeight()
        );
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets()
                .add("css/ui.css");
        stage.setScene(scene);
        stage.show();
    }

    private FxStackPane rootContainer() {
        var fxStackPane = new FxStackPane();
        fxStackPane.addOnCenter(qrCode());
        fxStackPane.addOnBottomCenter(label());
        return fxStackPane;
    }

    private FxLabel label() {
        var urlConnection = getUrlConnection();
        var label = new FxLabel(urlConnection);
        label.getStyleClass()
                .add("connection-label");
        return label;
    }

    private FxImageView qrCode() {
        var urlConnection = getUrlConnection();
        var qrCode = qrCodeGenerator.generateImage(300, 300, urlConnection)
                .orElseThrow();
        return new FxImageView(qrCode);
    }

    private String getUrlConnection() {
        var hostAddress = networkInterfaceFinder.getMainNetworkInterface();

        if (Strings.isBlank(hostAddress)) {
            return "No network interface found";
        }

        var serverPort = webServerAppContext.getWebServer()
                .getPort();

        return String.format("http://%s:%s/%n", hostAddress, serverPort);
    }
}