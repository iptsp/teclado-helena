package br.ipt.thl.os;

import br.ipt.thl.junit.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {NetworkInterfaceFinder.class})
class NetworkInterfaceFinderTest extends AbstractIntegrationTest {

    @Autowired
    private NetworkInterfaceFinder networkInterfaceFinder;

    @Test
    void checkMainNetworkInterface() {
        var result = networkInterfaceFinder.getMainNetworkInterface();
        assertFalse(result.isBlank());
    }

}
