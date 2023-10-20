package br.ipt.thl.qrcode;

import br.ipt.thl.common.func.Try;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

@Component
public class QRCodeGenerator {

    private final QRCodeWriter qrCodeWriter;

    public QRCodeGenerator() {
        this.qrCodeWriter = new QRCodeWriter();
    }

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
