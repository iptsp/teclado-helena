package br.ipt.thl.os;

import br.ipt.thl.common.func.Try;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Component
public class NetworkInterfaceResolver {

    public Optional<String> mainNetworkInterface() {

        return Try.of(() -> NetworkInterface.getNetworkInterfaces().asIterator())
                .map(networkInterfaces -> {
                    var spliterator = Spliterators.spliteratorUnknownSize(networkInterfaces,
                            Spliterator.ORDERED);

                    return StreamSupport.stream(spliterator, true)
                            .map(this::asNetworkInterfaceInfo)
                            .filter(NetworkInterfaceInfo::hasHostAddress)
                            .filter(NetworkInterfaceInfo::isLocalAddress)
                            .filter(NetworkInterfaceInfo::isUp)
                            .filter(NetworkInterfaceInfo::isNotVirtual)
                            .filter(NetworkInterfaceInfo::isNotLoopback)
                            .map(NetworkInterfaceInfo::hostAddress)
                            .findFirst();
                })
                .orElseThrow();
    }

    private NetworkInterfaceInfo asNetworkInterfaceInfo(final NetworkInterface networkInterface) {

        var displayName = networkInterface.getDisplayName();

        var hostAddress = Try.of(() -> networkInterface.getInetAddresses().asIterator())
                .map(networkInterfaces -> {
                    var spliterator = Spliterators.spliteratorUnknownSize(networkInterfaces,
                            Spliterator.ORDERED);
                    return StreamSupport.stream(spliterator, true)
                            .map(this::asInetAddressInfo)
                            .filter(InetAddressInfo::hasHostAddress)
                            .map(InetAddressInfo::hostAddress)
                            .findFirst()
                            .orElseThrow();

                })
                .orElse("");

        var isUp = Try.of(networkInterface::isUp)
                .orElse(false);
        var isVirtual = Try.of(networkInterface::isVirtual)
                .orElse(true);
        var isLoopback = Try.of(networkInterface::isLoopback)
                .orElse(true);

        return new NetworkInterfaceInfo(displayName, hostAddress, isUp, isVirtual, isLoopback);
    }

    private InetAddressInfo asInetAddressInfo(final InetAddress inetAddress) {
        var hostAddress = inetAddress.getHostAddress();
        return new InetAddressInfo(hostAddress);
    }

    record InetAddressInfo(String hostAddress) {
        public boolean hasHostAddress() {
            return !hostAddress.isBlank();
        }
    }

    record NetworkInterfaceInfo(String displayName, String hostAddress, boolean isUp, boolean isVirtual, boolean isLoopback) {

        public boolean hasHostAddress() {
            return !hostAddress.isBlank();
        }

        public boolean isLocalAddress() {
            return hostAddress.startsWith("192.") ||
                    hostAddress.startsWith("10.");
        }

        public boolean isNotVirtual() {
            return !isVirtual;
        }

        public boolean isNotLoopback() {
            return !isLoopback;
        }

    }

}
