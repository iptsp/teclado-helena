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

package br.ipt.thl.assets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MsixAssetGenerator {

    /**
     * Redimensiona o tamanho da imagem e a salva em um diretório definido
     *
     * @param inputFile     Caminho da imagem a ser redimensionada
     * @param width         Largura da imagem final
     * @param height        Altura da imagem final
     * @param outputFile    Caminho da imagem redimensionada
     * @throws IOException  Identifica erros de entrada, como o caminho do arquivo
     */
    private static void resize(final Path inputFile,
                               final int width,
                               final int height,
                               final Path outputFile) throws IOException {

        var image = javax.imageio.ImageIO.read(inputFile.toFile());
        var resized = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        var buffered = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(resized, 0, 0, null);
        javax.imageio.ImageIO.write(buffered, "png", outputFile.toFile());
        System.out.println("Generated: " + outputFile);

    }

    /**
     * Redimensiona e define os nomes, largura e altura das imagens que serão utilizadas pelo instalador MSIX
     *
     * @param args          Use launcherImage e outputFolder para definir o caminho da imagem de entrada
     *                      e o caminho onde as imagens processadas serão armazenadas
     * @throws IOException  Identifica erros de entrada, como o caminho do arquivo
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 2) throw new RuntimeException("Arguments <launcherImage> and <outputFolder> are required");

        var name = "THL";

        var inputImagePath = Path.of(args[0]);
        var outputDirPath = Path.of(args[1]);

        if (Files.notExists(inputImagePath))
            throw new RuntimeException("Input file does not exist: " + inputImagePath);

        if (!Files.isDirectory(outputDirPath))
            throw new RuntimeException("Invalid outputDir: " + outputDirPath);

        resize(inputImagePath, 44, 44, outputDirPath.resolve(name + "-Square44x44Logo.scale-100.png"));
        resize(inputImagePath, 55, 55, outputDirPath.resolve(name + "-Square44x44Logo.scale-125.png"));
        resize(inputImagePath, 66, 66, outputDirPath.resolve(name + "-Square44x44Logo.scale-150.png"));
        resize(inputImagePath, 88, 88, outputDirPath.resolve(name + "-Square44x44Logo.scale-200.png"));
        resize(inputImagePath, 176, 176, outputDirPath.resolve(name + "-Square44x44Logo.scale-400.png"));
        resize(inputImagePath, 16, 16, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-16.png"));
        resize(inputImagePath, 16, 16, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-16_altform-unplated.png"));
        resize(inputImagePath, 24, 24, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-24.png"));
        resize(inputImagePath, 24, 24, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-24_altform-unplated.png"));
        resize(inputImagePath, 32, 32, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-32.png"));
        resize(inputImagePath, 32, 32, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-32_altform-unplated.png"));
        resize(inputImagePath, 48, 48, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-48.png"));
        resize(inputImagePath, 48, 48, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-48_altform-unplated.png"));
        resize(inputImagePath, 256, 256, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-256.png"));
        resize(inputImagePath, 256, 256, outputDirPath.resolve(name + "-Square44x44Logo.targetsize-256_altform-unplated.png"));
        resize(inputImagePath, 71, 71, outputDirPath.resolve(name + "-Square71x71Logo.scale-100.png"));
        resize(inputImagePath, 89, 89, outputDirPath.resolve(name + "-Square71x71Logo.scale-125.png"));
        resize(inputImagePath, 107, 107, outputDirPath.resolve(name + "-Square71x71Logo.scale-150.png"));
        resize(inputImagePath, 142, 142, outputDirPath.resolve(name + "-Square71x71Logo.scale-200.png"));
        resize(inputImagePath, 284, 284, outputDirPath.resolve(name + "-Square71x71Logo.scale-400.png"));
        resize(inputImagePath, 150, 150, outputDirPath.resolve(name + "-Square150x150Logo.scale-100.png"));
        resize(inputImagePath, 188, 188, outputDirPath.resolve(name + "-Square150x150Logo.scale-125.png"));
        resize(inputImagePath, 225, 225, outputDirPath.resolve(name + "-Square150x150Logo.scale-150.png"));
        resize(inputImagePath, 300, 300, outputDirPath.resolve(name + "-Square150x150Logo.scale-200.png"));
        resize(inputImagePath, 600, 600, outputDirPath.resolve(name + "-Square150x150Logo.scale-400.png"));
        resize(inputImagePath, 310, 310, outputDirPath.resolve(name + "-Square310x310Logo.scale-100.png"));
        resize(inputImagePath, 388, 388, outputDirPath.resolve(name + "-Square310x310Logo.scale-125.png"));
        resize(inputImagePath, 465, 465, outputDirPath.resolve(name + "-Square310x310Logo.scale-150.png"));
        resize(inputImagePath, 620, 620, outputDirPath.resolve(name + "-Square310x310Logo.scale-200.png"));
        resize(inputImagePath, 1240, 1240, outputDirPath.resolve(name + "-Square310x310Logo.scale-400.png"));
        resize(inputImagePath, 310, 150, outputDirPath.resolve(name + "-Wide310x150Logo.scale-100.png"));
        resize(inputImagePath, 388, 188, outputDirPath.resolve(name + "-Wide310x150Logo.scale-125.png"));
        resize(inputImagePath, 465, 225, outputDirPath.resolve(name + "-Wide310x150Logo.scale-150.png"));
        resize(inputImagePath, 620, 300, outputDirPath.resolve(name + "-Wide310x150Logo.scale-200.png"));
        resize(inputImagePath, 1240, 600, outputDirPath.resolve(name + "-Wide310x150Logo.scale-400.png"));
        resize(inputImagePath, 50, 50, outputDirPath.resolve("StoreLogo.scale-100.png"));
        resize(inputImagePath, 63, 63, outputDirPath.resolve("StoreLogo.scale-125.png"));
        resize(inputImagePath, 75, 75, outputDirPath.resolve("StoreLogo.scale-150.png"));
        resize(inputImagePath, 100, 100, outputDirPath.resolve("StoreLogo.scale-200.png"));
        resize(inputImagePath, 200, 200, outputDirPath.resolve("StoreLogo.scale-400.png"));

    }
}
