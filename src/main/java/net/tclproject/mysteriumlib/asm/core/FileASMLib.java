package net.tclproject.mysteriumlib.asm.core;

import org.apache.commons.io.*;
import java.io.*;
import java.util.*;

public class FileASMLib
{
    File originalClasses;
    File fixesDir;

    public FileASMLib() {
        this.originalClasses = new File("classes");
        this.fixesDir = new File("fixes");
    }

    public static void main(final String[] args) throws IOException {
        new FileASMLib().transform();
    }

    void transform() throws IOException {
        final TargetClassTransformer transformer = new TargetClassTransformer();
        for (final File file : getFiles(".class", this.fixesDir)) {
            transformer.registerClassWithFixes(FileUtils.readFileToByteArray(file));
        }
        for (final File file : getFiles(".class", this.originalClasses)) {
            final byte[] bytes = IOUtils.toByteArray((InputStream)new FileInputStream(file));
            final String className = "";
            transformer.transform(className, bytes);
        }
    }

    private static List<File> getFiles(final String extension, final File directory) throws IOException {
        final ArrayList<File> files = new ArrayList<File>();
        final File[] filesArray = directory.listFiles();
        if (filesArray != null) {
            for (final File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    files.addAll(getFiles(extension, file));
                }
                else if (file.getName().toLowerCase().endsWith(extension)) {
                    files.add(file);
                }
            }
        }
        return files;
    }
}
