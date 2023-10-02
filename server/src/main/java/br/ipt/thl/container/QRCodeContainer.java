package br.ipt.thl.container;

import br.ipt.thl.fx.FxImageView;
import br.ipt.thl.fx.FxLabel;
import br.ipt.thl.fx.FxStackPane;
import br.ipt.thl.os.NetworkInterfaceResolver;
import br.ipt.thl.qrcode.QRCodeGenerator;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class QRCodeContainer {

    private final ServletWebServerApplicationContext webServerAppContext;
    private final NetworkInterfaceResolver networkInterfaceResolver;
    private final QRCodeGenerator qrCodeGenerator;

    public QRCodeContainer(final ServletWebServerApplicationContext webServerAppContext,
                           final NetworkInterfaceResolver networkInterfaceResolver,
                           final QRCodeGenerator qrCodeGenerator) {
        this.webServerAppContext = webServerAppContext;
        this.networkInterfaceResolver = networkInterfaceResolver;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    private String getUrlConnection() {
        var serverPort = webServerAppContext.getWebServer()
                .getPort();
        return networkInterfaceResolver.mainNetworkInterface()
                .map(address -> String.format("http://%s:%s/%n", address, serverPort))
                .orElse("No network interface found");
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

    public FxStackPane rootContainer() {
        var fxStackPane = new FxStackPane();
        fxStackPane.addOnCenter(qrCode());
        fxStackPane.addOnBottomCenter(label());
        return fxStackPane;
    }

}
