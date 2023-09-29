package br.ipt.thl.qrcode;

import br.ipt.thl.junit.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {QRCodeGenerator.class})
class QRCodeGeneratorTest extends AbstractIntegrationTest {

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Test
    void generateImage() {
        var result = qrCodeGenerator.generateImage(100, 100, "test");
        assertTrue(result.isSuccess());
    }
}
