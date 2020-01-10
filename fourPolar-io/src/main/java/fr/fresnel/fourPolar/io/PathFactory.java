package fr.fresnel.fourPolar.io;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to store or read data.
 */
public class PathFactory {
    private static String root = "4Polar";  // Root of where we start to put files.
    private static String folder_params = "0_Folder";   // Keeps the data about setup, files etc.

    /**
     * Returns the path to the 0_Params folder.
     * @param rootFolder
     * @return
     */
    public void create_4Polar_Folder(File rootFolder) {
        new File(rootFolder, root).mkdir();
    }

    public static File getFolder_0_Params(File rootFolder) {
        File file = new File(rootFolder, Paths.get(root, folder_params).toString());
        file.mkdirs();
        return file;
    }

    public static File getFolder_sampleImagesTemplateExcelFiles(File rootFolder) {
        return new File(rootFolder.getAbsolutePath());
    }

}