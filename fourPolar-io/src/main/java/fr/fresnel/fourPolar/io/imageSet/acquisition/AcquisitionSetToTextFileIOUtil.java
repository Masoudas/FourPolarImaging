package fr.fresnel.fourPolar.io.imageSet.acquisition;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSetType;

/**
 * A set of utility method for text file IO.
 */
class AcquisitionSetToTextFileIOUtil {
    private AcquisitionSetToTextFileIOUtil() {
        throw new AssertionError();
    }

    /**
     * @return the root folder of where all the text files corresponding to the
     *         acquisition set are written.
     */
    public static File _getTextFilesRootFolder(File root4PProject, AcquisitionSetType setType) {
        return new File(PathFactoryOfProject.getFolder_Params(root4PProject), setType.description);
    }

    /**
     * @return the text file that corresponds to this set name.
     */
    public static File _getCapturedSetTextFile(File textFilesRoot, String setName) {
        return new File(textFilesRoot, setName + ".txt");
    }

}