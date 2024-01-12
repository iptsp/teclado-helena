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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Files {

    private static final Logger LOGGER = LoggerFactory.getLogger(Files.class);
    private static final String SPRING_BOOT_INF_CLASSES = "BOOT-INF/classes!/";

    private Files() {

    }

    public static Path tempDir() {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }


    public static Try<Path> copyFromClasspathToDir(final String resource,
                                                   final String name,
                                                   final boolean overwrite,
                                                   final Path targetDir) {
        Path target = targetDir.resolve(name);
        if (exists(target) && !overwrite) {
            return Try.success(target);
        }
        deleteRecursively(target)
                .orElseThrow();
        return copy(resource, target);
    }

    public static Try<byte[]> readAllBytes(final Path path) {
        return Try.of(() -> java.nio.file.Files.readAllBytes(path.normalize()));
    }

    public static Try<Path> deleteRecursively(final Path target) {
        if (!exists(target)) {
            return Try.success(target);
        }
        return Try.of(() -> java.nio.file.Files.walkFileTree(target, new FileRecursiveDelete()));
    }

    public static Try<Path> copyRecursively(final Path source,
                                            final Path target) {
        return Try.of(() -> {
            java.nio.file.Files.walkFileTree(source, new FileRecursiveCopy(source, target));
            return target;
        });
    }

    private static boolean isInsideSpringBoot(final URI uri) {
        return uri.toString().contains(SPRING_BOOT_INF_CLASSES);
    }

    private static String jarFileSystemPath(final URI uri) {
        if (isInsideSpringBoot(uri)) {
            var uriAsString = uri.toString();
            var len = SPRING_BOOT_INF_CLASSES.length();
            var fixedUri = uriAsString.substring(uriAsString.lastIndexOf(SPRING_BOOT_INF_CLASSES) + len);
            String decodedFixedUri = decode(fixedUri);
            return Paths.get("BOOT-INF", "classes", decodedFixedUri)
                    .toString();
        }
        return Paths.get(uri).toString();
    }

    private static String decode(final String path) {
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }

    private static Try<Path> copy(final String resource,
                                  final Path target) {

        var uri = Classpaths.uri(resource)
                .orElseThrow();
        var scheme = uri.getScheme();

        if ("jar".equals(scheme)) {
            return Try.withResources(() -> FileSystems.newFileSystem(uri, Collections.emptyMap()))
                    .of(fileSystem -> {
                        Path source = fileSystem.getPath(jarFileSystemPath(uri));
                        return copyRecursively(source, target)
                                .orElseThrow();
                    });
        }

        return copyRecursively(Paths.get(uri), target);
    }

    public static Try<Long> sizeOfPath(final Path path) {

        return Try.withResources(() -> java.nio.file.Files.walk(path))
                .of(stream -> stream
                        .map(Path::toFile)
                        .filter(File::isFile)
                        .mapToLong(File::length)
                        .sum()
                );
    }

    public static boolean exists(final Path path) {
        return java.nio.file.Files.exists(path);
    }

    public static Try<Path> unzip(final Path path,
                                  final boolean overwrite) {

        var fileName = path.getFileName().toString();
        var destination = path.getParent().resolve(fileName.substring(0, fileName.lastIndexOf(".")));

        if (Files.exists(destination) && !overwrite) {
            LOGGER.debug("Path {} already unzipped", destination);
            return Try.success(destination);
        }

        if (overwrite) {
            LOGGER.debug("Overwriting path {}", destination);
            deleteRecursively(destination)
                    .orElseThrow();
        }

        LOGGER.debug("Unzipping {} to {}", path, destination);

        var outputDir = destination.toFile();

        var thresholdMaxEntries = 20000;
        var thresholdMaxSize = (long) 2 * 1024 * 1024 * 1024; //2GB
        var thresholdMaxRatio = 500;

        return Try.withResources(() -> new ZipFile(path.toFile()))
                .flatMap(zipFile -> {

                    long totalSizeArchive = 0;
                    long totalEntryArchive = 0;

                    for (ZipEntry zipEntry : Collections.list(zipFile.entries())) {
                        File entryDestination = new File(outputDir, zipEntry.getName());
                        if (zipEntry.isDirectory()) {
                            if (!entryDestination.mkdirs()) {
                                return Try.failure(new IOException("Failed to create directory " + entryDestination));
                            }
                        } else {
                            var inputStream = zipFile.getInputStream(zipEntry);
                            var outputStream = new FileOutputStream(entryDestination);
                            var nBytes = inputStream.available();

                            var compressedSize = zipEntry.getCompressedSize();
                            if (compressedSize > 0) {
                                var compressionRatio = nBytes / compressedSize;
                                if (compressionRatio > thresholdMaxRatio) {
                                    return Try.failure(new IllegalStateException("Compression ratio is too high: " + compressionRatio));
                                }
                            }

                            try (inputStream; outputStream) {
                                inputStream.transferTo(outputStream);
                            }

                            totalEntryArchive++;
                            totalSizeArchive += nBytes;

                            if (totalSizeArchive > thresholdMaxSize) {
                                return Try.failure(new IllegalStateException("Archive is too big"));
                            }

                            if (totalEntryArchive > thresholdMaxEntries) {
                                return Try.failure(new IllegalStateException("Archive has too much entries"));
                            }
                        }
                    }
                    return Try.success(destination);
                });

    }

    public static String fileName(final Path path) {
        return path.getFileName().toString();
    }

    public static boolean isDir(final Path path) {
        return java.nio.file.Files.isDirectory(path);
    }

    public static Try<URL> url(final Path path) {
        return Try.of(() -> path.toUri().toURL());
    }

    public static Path userLocalAppData() {
        return Paths.get(System.getenv("localappdata"))
                .normalize();
    }


    public static Try<byte[]> chunkOf(final Path path,
                                      final long startIndex,
                                      final long endIndex) {

        return sizeOfPath(path)
                .flatMap(size -> {
                    return Try.withResources(() -> new RandomAccessFile(path.toFile(), "r"))
                            .of(randomAccessFile -> {
                                var start = Math.max(0, startIndex);
                                var end = Math.min(size - 1, endIndex);
                                var length = (end - start) + 1;
                                var bytes = new byte[(int) length];
                                randomAccessFile.seek(start);
                                randomAccessFile.readFully(bytes);
                                return bytes;
                            });
                });
    }

    public static boolean notExists(final Path path) {
        return !exists(path);
    }

    public static Try<Path> createDirectories(final Path path) {
        return Try.of(() -> java.nio.file.Files.createDirectories(path));
    }

    public static Try<Long> copy(final InputStream inputStream,
                                 final Path path) {
        return Try.of(() -> java.nio.file.Files.copy(inputStream, path));
    }

    public static Try<String> fileNameWithoutExtension(final Path path) {
        if (Files.isDir(path)) {
            return Try.failure(new IllegalArgumentException("Path %s is a directory".formatted(path)));
        }
        var fileName = fileName(path);
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return Try.success(fileName);
        }
        var extension = fileName.substring(0, lastIndexOf);
        return Try.success(extension);
    }

    public static Try<List<Path>> list(final Path path) {
        return Try.withResources(() -> java.nio.file.Files.list(path))
                .of(Stream::toList);
    }

    public static Path tempFile(final String fileName) {
        return tempDir().resolve(fileName);
    }

    public static Try<InputStream> newInputStream(final Path path) {
        return Try.of(() -> java.nio.file.Files.newInputStream(path));
    }

    public static Try<OutputStream> newOutputStream(final Path path) {
        return Try.of(() -> java.nio.file.Files.newOutputStream(path));
    }

    public static Path resolve(final Path source,
                               final Path destination,
                               final Path path) {
        var relativize = source.relativize(path);
        var isEmptyPath = Files.isEmptyPath(relativize);
        if (isEmptyPath) {
            return destination;
        }
        return destination.resolve(relativize.toString());
    }

    public static boolean isEmptyPath(final Path path) {
        if (path == null) {
            return true;
        }
        return Strings.isEmpty(path.toString());
    }

    public static Try<Path> ensureDir(final Path path) {
        if (Files.notExists(path)) {
            return Files.createDirectories(path);
        }
        return Try.success(path);
    }

    public static String rawAsString(final Path path){
        return path.toString();
    }

    public static String uriAbsoluteAsString(final Path path) {
        var normalizedAbsolutePath = path.toAbsolutePath().normalize();
        return Files.uriAsString(normalizedAbsolutePath);
    }

    public static String uriAsString(final Path path) {
        return String.valueOf(uri(path));
    }

    public static URI uri(final Path path) {
        return path.toUri();
    }

    public static String absolutePath(final Path path) {
        return String.valueOf(path.toAbsolutePath());
    }


    public static String extensionOf(final Path path) {
        if (path == null) {
            return "";
        }
        return extensionOf(path.getFileName().toString());
    }

    public static String extensionOf(final String fileName) {

        if (Strings.isBlank(fileName)) {
            return "";
        }

        var lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf != -1) {
            return fileName.substring(lastIndexOf + 1);
        }
        return "";
    }

    public static Try<Path> createNewWithContent(final Path path,
                                                 final String data) {
        return createNewWithContent(path, data.getBytes(StandardCharsets.UTF_8));
    }

    public static Try<Path> createNewWithContent(final Path path,
                                                 final byte[] data) {
        return Try.of(() -> java.nio.file.Files.write(path, data, StandardOpenOption.CREATE_NEW));
    }

    public static Try<Path> createWithContent(final Path path,
                                              final byte[] data) {
        return Try.of(() -> java.nio.file.Files.write(path, data, StandardOpenOption.CREATE));
    }

    public static Try<String> readAsString(final Path path) {
        return Try.of(() -> java.nio.file.Files.readString(path, StandardCharsets.UTF_8));
    }

    public static Try<byte[]> asBytes(final Path path) {
        return Try.of(() -> java.nio.file.Files.readAllBytes(path));
    }

    private static class FileRecursiveDelete extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult postVisitDirectory(final Path dir,
                                                  final IOException exc) throws IOException {
            java.nio.file.Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path path,
                                         final BasicFileAttributes basicFileAttributes) throws IOException {
            java.nio.file.Files.delete(path);
            return FileVisitResult.CONTINUE;
        }
    }

    private static class FileRecursiveCopy extends SimpleFileVisitor<Path> {
        private final Path source;
        private final Path destination;

        public FileRecursiveCopy(final Path source,
                                 final Path destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path path,
                                                 final BasicFileAttributes basicFileAttributes) throws IOException {
            var newDir = resolve(source, destination, path);
            java.nio.file.Files.createDirectories(newDir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path path,
                                         final BasicFileAttributes basicFileAttributes) throws IOException {
            if (java.nio.file.Files.isDirectory(destination)) {
                var newDir = resolve(source, destination, path);
                java.nio.file.Files.copy(path, newDir);
            } else {
                java.nio.file.Files.copy(path, destination);
            }
            return FileVisitResult.CONTINUE;
        }
    }

}
