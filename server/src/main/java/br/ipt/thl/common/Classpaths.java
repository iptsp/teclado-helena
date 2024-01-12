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

package br.ipt.thl.common;

import br.ipt.thl.common.func.Try;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class Classpaths {

    private Classpaths() {

    }

    public static Try<Path> tryPath(final String resource) {
        return file(resource)
                .map(File::toPath);
    }

    public static Path path(final String resource) {
        return file(resource)
                .map(File::toPath)
                .orElseThrow();
    }

    private static Try<File> file(final String resource) {
        return Try.of(() -> classpathResource(resource).getFile());
    }

    private static Resource classpathResource(final String resource) {
        return new ClassPathResource(resource, classLoader());
    }

    public static String urlAsString(final String path) {
        return url(path)
                .map(URL::toString)
                .orElseThrow();
    }

    public static Try<InputStream> inputStream(final String resource) {
        return Try.of(() -> classpathResource(resource).getInputStream());
    }

    public static Try<byte[]> asBytes(final String resource) {
        return Try.withResources(ByteArrayOutputStream::new)
                .flatMap(byteArrayOutputStream -> transfer(resource, byteArrayOutputStream));
    }

    private static Try<byte[]> transfer(final String resource,
                                        final ByteArrayOutputStream byteArrayOutputStream) {
        return inputStream(resource)
                .map(inputStream -> {
                    inputStream.transferTo(byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                });
    }

    public static Try<String> asString(final String resource) {
        return asBytes(resource)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8));
    }

    public static Try<URL> url(final String resource) {
        return Try.of(() -> classpathResource(resource).getURL());
    }

    public static Try<URI> uri(final String resource) {
        return Try.of(() -> classpathResource(resource).getURI());
    }

    public static String toExternalForm(final String resource) {
        return url(resource)
                .map(URL::toExternalForm)
                .orElseThrow();
    }

    public static ClassLoader classLoader() {
        return Classpaths.class.getClassLoader();
    }
}
