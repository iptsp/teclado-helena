package br.ipt.thl.assets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MsixAssetGenerator {

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

    public static void main(String[] args) throws IOException {

        if (args.length != 1) throw new RuntimeException("Arguments <projectDir> ir required");

        var name = "THL";

        var baseDirPath = Path.of(args[0]);
        if (!Files.isDirectory(baseDirPath))
            throw new RuntimeException("Invalid projectDir: " + baseDirPath);


        var inputFile = baseDirPath.resolve("server\\jpackage\\launcher.png");
        var outDir = baseDirPath.resolve("server\\jpackage\\Assets");

        resize(inputFile, 44, 44, outDir.resolve(name + "-Square44x44Logo.scale-100.png"));
        resize(inputFile, 55, 55, outDir.resolve(name + "-Square44x44Logo.scale-125.png"));
        resize(inputFile, 66, 66, outDir.resolve(name + "-Square44x44Logo.scale-150.png"));
        resize(inputFile, 88, 88, outDir.resolve(name + "-Square44x44Logo.scale-200.png"));
        resize(inputFile, 176, 176, outDir.resolve(name + "-Square44x44Logo.scale-400.png"));
        resize(inputFile, 16, 16, outDir.resolve(name + "-Square44x44Logo.targetsize-16.png"));
        resize(inputFile, 16, 16, outDir.resolve(name + "-Square44x44Logo.targetsize-16_altform-unplated.png"));
        resize(inputFile, 24, 24, outDir.resolve(name + "-Square44x44Logo.targetsize-24.png"));
        resize(inputFile, 24, 24, outDir.resolve(name + "-Square44x44Logo.targetsize-24_altform-unplated.png"));
        resize(inputFile, 32, 32, outDir.resolve(name + "-Square44x44Logo.targetsize-32.png"));
        resize(inputFile, 32, 32, outDir.resolve(name + "-Square44x44Logo.targetsize-32_altform-unplated.png"));
        resize(inputFile, 48, 48, outDir.resolve(name + "-Square44x44Logo.targetsize-48.png"));
        resize(inputFile, 48, 48, outDir.resolve(name + "-Square44x44Logo.targetsize-48_altform-unplated.png"));
        resize(inputFile, 256, 256, outDir.resolve(name + "-Square44x44Logo.targetsize-256.png"));
        resize(inputFile, 256, 256, outDir.resolve(name + "-Square44x44Logo.targetsize-256_altform-unplated.png"));
        resize(inputFile, 71, 71, outDir.resolve(name + "-Square71x71Logo.scale-100.png"));
        resize(inputFile, 89, 89, outDir.resolve(name + "-Square71x71Logo.scale-125.png"));
        resize(inputFile, 107, 107, outDir.resolve(name + "-Square71x71Logo.scale-150.png"));
        resize(inputFile, 142, 142, outDir.resolve(name + "-Square71x71Logo.scale-200.png"));
        resize(inputFile, 284, 284, outDir.resolve(name + "-Square71x71Logo.scale-400.png"));
        resize(inputFile, 150, 150, outDir.resolve(name + "-Square150x150Logo.scale-100.png"));
        resize(inputFile, 188, 188, outDir.resolve(name + "-Square150x150Logo.scale-125.png"));
        resize(inputFile, 225, 225, outDir.resolve(name + "-Square150x150Logo.scale-150.png"));
        resize(inputFile, 300, 300, outDir.resolve(name + "-Square150x150Logo.scale-200.png"));
        resize(inputFile, 600, 600, outDir.resolve(name + "-Square150x150Logo.scale-400.png"));
        resize(inputFile, 310, 310, outDir.resolve(name + "-Square310x310Logo.scale-100.png"));
        resize(inputFile, 388, 388, outDir.resolve(name + "-Square310x310Logo.scale-125.png"));
        resize(inputFile, 465, 465, outDir.resolve(name + "-Square310x310Logo.scale-150.png"));
        resize(inputFile, 620, 620, outDir.resolve(name + "-Square310x310Logo.scale-200.png"));
        resize(inputFile, 1240, 1240, outDir.resolve(name + "-Square310x310Logo.scale-400.png"));
        resize(inputFile, 310, 150, outDir.resolve(name + "-Wide310x150Logo.scale-100.png"));
        resize(inputFile, 388, 188, outDir.resolve(name + "-Wide310x150Logo.scale-125.png"));
        resize(inputFile, 465, 225, outDir.resolve(name + "-Wide310x150Logo.scale-150.png"));
        resize(inputFile, 620, 300, outDir.resolve(name + "-Wide310x150Logo.scale-200.png"));
        resize(inputFile, 1240, 600, outDir.resolve(name + "-Wide310x150Logo.scale-400.png"));
        resize(inputFile, 50, 50, outDir.resolve("StoreLogo.scale-100.png"));
        resize(inputFile, 63, 63, outDir.resolve("StoreLogo.scale-125.png"));
        resize(inputFile, 75, 75, outDir.resolve("StoreLogo.scale-150.png"));
        resize(inputFile, 100, 100, outDir.resolve("StoreLogo.scale-200.png"));
        resize(inputFile, 200, 200, outDir.resolve("StoreLogo.scale-400.png"));


    }
}
