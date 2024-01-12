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
                            .filter(InetAddressInfo::isSiteLocalAddress)
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
        var isSiteLocalAddress = inetAddress.isSiteLocalAddress();
        return new InetAddressInfo(hostAddress, isSiteLocalAddress);
    }

    record InetAddressInfo(String hostAddress, boolean isSiteLocalAddress) {
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
