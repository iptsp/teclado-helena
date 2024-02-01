//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

package br.ipt.thl.container;

import br.ipt.thl.fx.FxImageView;
import br.ipt.thl.fx.FxLabel;
import br.ipt.thl.fx.FxStackPane;
import br.ipt.thl.os.NetworkInterfaceResolver;
import br.ipt.thl.qrcode.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Container da tela do QRCode.
 */
@Component
public class QRCodeContainer extends FxStackPane {

    /** Porta do servidor. Ex.: 8080 */
    private final Integer serverPort;
    /** Interface de conexão. */
    private final NetworkInterfaceResolver networkInterfaceResolver;
    /** Gerador de QRCode */
    private final QRCodeGenerator qrCodeGenerator;

    @Autowired
    public QRCodeContainer(@Value("${server.port}") final Integer serverPort,
                           final NetworkInterfaceResolver networkInterfaceResolver,
                           final QRCodeGenerator qrCodeGenerator) {

        this.serverPort = serverPort;
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
                .map(address -> String.format("http://%s:%s", address, serverPort))
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
