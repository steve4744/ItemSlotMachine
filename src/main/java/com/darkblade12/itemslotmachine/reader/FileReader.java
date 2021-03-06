package com.darkblade12.itemslotmachine.reader;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class FileReader {

    private final String resourceFileName;
    private final String outputFileName;
    final String outputPath;
    final File outputFile;

    FileReader(String resourceFileName, String outputFileName, String outputPath) {
        this.resourceFileName = resourceFileName;
        this.outputFileName = outputFileName;
        if (!outputPath.endsWith("/")) {
            outputPath += "/";
        }
        this.outputPath = outputPath;
        outputFile = new File(outputPath + outputFileName);
    }

    FileReader(String fileName, String outputPath) {
        this(fileName, fileName, outputPath);
    }

    void deleteFile() {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    boolean saveResourceFile(Plugin plugin) {
        InputStream in = plugin.getResource(resourceFileName);
        if (in == null) {
            return false;
        }
        new File(outputPath).mkdirs();
        try {
            OutputStream out = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            for (int length = in.read(buffer); length > 0; length = in.read(buffer)) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getResourceFileName() {
        return resourceFileName;
    }

    public String getOuputFileName() {
        return outputFileName;
    }

    public String getOuputPath() {
        return outputPath;
    }

    public File getOuputFile() {
        return outputFile;
    }
}
