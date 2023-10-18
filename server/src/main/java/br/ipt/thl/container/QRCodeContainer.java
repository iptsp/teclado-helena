package br.ipt.thl.container;

import br.ipt.thl.fx.FxImageView;
import br.ipt.thl.fx.FxLabel;
import br.ipt.thl.fx.FxStackPane;
import br.ipt.thl.os.NetworkInterfaceResolver;
import br.ipt.thl.qrcode.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QRCodeContainer extends FxStackPane {

    private final Integer uiPort;
    private final NetworkInterfaceResolver networkInterfaceResolver;
    private final QRCodeGenerator qrCodeGenerator;

    @Autowired
    public QRCodeContainer(@Value("${ui.port}") final Integer uiPort,
                           final NetworkInterfaceResolver networkInterfaceResolver,
                           final QRCodeGenerator qrCodeGenerator) {

        this.uiPort = uiPort;
        this.networkInterfaceResolver = networkInterfaceResolver;
        this.qrCodeGenerator = qrCodeGenerator;
        doAssemble();
        doStyle();
    }

    private void doStyle() {
        addStyleClass("qr-code-container");
    }

    private void doAssemble() {
        addOnCenter(qrCode());
        addOnBottomCenter(label());
    }

    private String urlToConnect() {
        return networkInterfaceResolver.mainNetworkInterface()
                .map(address -> String.format("http://%s:%s/", address, uiPort))
                .orElse("No network interface found");
    }

    private FxLabel label() {
        var urlConnection = urlToConnect();
        var fxLabel = new FxLabel(urlConnection);
        fxLabel.addStyleClass("connection-label");
        return fxLabel;
    }

    private FxImageView qrCode() {
        var urlConnection = urlToConnect();
        var qrCode = qrCodeGenerator.createAsPng(300, 300, urlConnection)
                .orElseThrow();
        return new FxImageView(qrCode, 300, 300);
    }

}
