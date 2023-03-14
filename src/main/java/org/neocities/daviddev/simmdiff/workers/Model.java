package org.neocities.daviddev.simmdiff.workers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.neocities.daviddev.simmdiff.core.ExtendedNTA;
import org.neocities.daviddev.simmdiff.grammars.uppaal.FileLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class Model implements Callable<ExtendedNTA> {

    private File modelFile;
    public Model(File model) throws IOException {
        //modelFile = new File(cleanedFileName);
        modelFile = model;
        writeCleanedFile(modelFile);
    }

    private synchronized void writeCleanedFile(File file) throws IOException {
        FileLoader fileLoader = new FileLoader(file);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileLoader.getParsedContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ExtendedNTA call() {
        return new ExtendedNTA(modelFile.getPath());
    }
}
