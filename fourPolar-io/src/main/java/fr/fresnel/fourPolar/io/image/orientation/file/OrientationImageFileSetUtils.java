package fr.fresnel.fourPolar.io.image.orientation.file;

import java.io.File;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

/**
 * A set of static helper methods.
 */
class OrientationImageFileSetUtils {
    public OrientationImageFileSetUtils() {
        throw new AssertionError();
    }

    /**
     * Creates the parent folder path for an orientation image.
     * 
     * @param root4PProject is the root of where all images are stored for the
     *                      project.
     * @param channel       is the channel number.
     * @param fileSet       is the set to which this orientation image belong.
     * @return
     */
    public static File getParentFolder(File root4PProject, int channel, ICapturedImageFileSet fileSet) {
        return Paths.get(PathFactoryOfProject.getFolder_OrientationImages(root4PProject).getAbsolutePath(),
                "Channel " + channel, fileSet.getSetName()).toFile();
    }

    public static File createRhoImageFile(File parentFolder, String extension) {
        return new File(parentFolder, "Rho" + "." + extension);
        
    }

    public static File createDeltaImageFile(File parentFolder, String extension) {
        return new File(parentFolder, "Delta" + "." + extension);
        
    }

    public static File createEtaImageFile(File parentFolder, String extension) {
        return new File(parentFolder, "Eta" + "." + extension);
    }
}