package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

class SCIFIOUtils {
    /**
     * Check file extension is tif or tiff.
     * 
     * @param fileName
     * @throws IOException
     */
    public static void checkExtension(String fileName) throws IOException {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!extension.equals("tiff") && !extension.equals("tif")) {
            throw new IOException("The given file is not tiff");
        }

    }

    public static void checkFileExists(File path) throws IOException {
        if (!path.exists()) {
            throw new IOException("The given Tiff file does not exist.");
        }
    }

    public static void deleteFileIfExists(File path) {
        if (path.exists()) {
            path.delete();
        }
    }

}