package com.frahhs.robbing.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileUtil {
    public static boolean isFileEmpty(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Cannot check the file length. The file is not found: " + file.getAbsolutePath());
        }
        return file.length() == 0;
    }

    public static void toTarGzipFile(File file) {
        Path path = Paths.get(file.getPath());
        Path output = Paths.get(file.getPath().replace(".log", "") + ".tar.gz");

        try (OutputStream fOut = Files.newOutputStream(output);
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

            TarArchiveEntry tarEntry = new TarArchiveEntry(
                    path.toFile(),
                    path.getFileName().toString()
            );

            tOut.putArchiveEntry(tarEntry);
            Files.copy(path, tOut);
            tOut.closeArchiveEntry();
            tOut.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!file.delete())
            throw new RuntimeException("failed to remove log file after compression: " + file.getPath());
    }
}
