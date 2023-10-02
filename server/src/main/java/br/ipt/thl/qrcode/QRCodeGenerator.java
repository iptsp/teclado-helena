package br.ipt.thl.qrcode;

import br.ipt.thl.common.func.Try;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    private static final String QRCODE_IMAGE_PATH = "qrcode.png";

    public Try<Path> generateImage(int height, int width, String text) {

        var qrCodeWriter = new QRCodeWriter();
        var path = FileSystems.getDefault().getPath(QRCODE_IMAGE_PATH);
        var bitMatrix = Try.of(() -> qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height))
                .orElseThrow();

        try {
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (IOException e) {
            return Try.failure(e);
        }

        return Try.success(path);
    }
}
