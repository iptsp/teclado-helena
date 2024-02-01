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

package br.ipt.thl.qrcode;

import br.ipt.thl.common.func.Try;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

/**
 * Gerador de QRCode.
 */
@Component
public class QRCodeGenerator {

    /** Gerador de QRCode do java. */
    private final QRCodeWriter qrCodeWriter;

    /**  */
    public QRCodeGenerator() {
        this.qrCodeWriter = new QRCodeWriter();
    }

    /**
     * Converte um texto simples em um QRCode.
     * @param height    Altura da imagem do QRCode.
     * @param width     Largura da imagem do QRCode.
     * @param text      Texto a ser convertido em QRCode.
     * @return          Imagem em bytes do QRCode.
     */
    public Try<byte[]> createAsPng(int height,
                                   int width,
                                   final String text) {
        return Try.of(() -> qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height))
                .map(bitMatrix -> {
                    try (var byteArrOutputStream = new java.io.ByteArrayOutputStream()) {
                        MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrOutputStream);
                        return byteArrOutputStream.toByteArray();
                    }
                });
    }
}
