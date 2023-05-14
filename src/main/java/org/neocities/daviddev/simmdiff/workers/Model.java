package org.neocities.daviddev.simmdiff.workers;

import org.neocities.daviddev.simmdiff.core.ExtendedNTA;
import org.neocities.daviddev.simmdiff.grammars.uppaal.FileLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

public class Model implements Callable<ExtendedNTA> {

    private File modelFile;
    private FileLoader fileLoader;
    public Model(File model) throws IOException {
        modelFile = model;
        writeCleanedFile(modelFile);
    }

    private synchronized void writeCleanedFile(File file) throws IOException {
        fileLoader = new FileLoader(file);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileLoader.getParsedContent());
        } catch (IOException e) {
            System.err.printf("Error parsing or writing using the file %s, :%s\n", file.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("wrote cleaned file " + file.getPath());
    }
    @Override
    public ExtendedNTA call() {
        return new ExtendedNTA(modelFile.getPath(), fileLoader.getChanDict());
    }
}
