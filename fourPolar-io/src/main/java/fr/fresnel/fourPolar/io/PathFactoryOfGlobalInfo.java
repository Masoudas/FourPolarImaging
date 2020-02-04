package fr.fresnel.fourPolar.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to write the global (project independent) information 
 * of the software (including user setting, databases and so forth).
 * 
 */
public class PathFactoryOfGlobalInfo {
    private static File folder_softwarePrivateRoot = setFolder_softwarePrivateDataRoot(); // This is the private
                                                                                              // root on the home
                                                                                              // folder, where we keep
                                                                                              // setting information.
                                                                                              // The final folder is
                                                                                              // called 4PolarSoftware
                                                                                              // or .4PolarSoftware


    /**
     * This method is used for creating the private folder in the home folder, which
     * contains information such as database and settings.
     * 
     * @return
     */
    private static File setFolder_softwarePrivateDataRoot() {
        String homeFolder = System.getProperty("user.home");
        String folderName = "4PolarSoftware";

        // Create the hidden root folder based on the OS.
        File rootFolder;
        if (System.getProperty("os.name").contains("windows")) {
            Path rootPath = Paths.get(homeFolder, folderName);
            try {
                Files.setAttribute(rootPath, "dos:hidden", true);
            } catch (IOException e) {
                System.out.println("Unable to create software hidden folder.");
            }
            rootFolder = rootPath.toFile();
        } else {
            rootFolder = new File(homeFolder, "." + folderName);
        }

        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        return rootFolder;
    }

    /**
     * Returns the path to the Data folder in the hidden home folder.
     */
    public static File getFolder_Data() {
        return new File(folder_softwarePrivateRoot, "Data");
    }

}