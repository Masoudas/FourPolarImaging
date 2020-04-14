package fr.fresnel.fourPolar.io.image.polarization.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImageFileSet} for
 * grouping a set of polariozation image files.
 */
public class TiffPolarizationImageFileSet implements IPolarizationImageFileSet {
    final private static String _pol0 = "Polarization0.tif";
    final private static String _pol45 = "Polarization45.tif";
    final private static String _pol90 = "Polarization90.tif";
    final private static String _pol135 = "Polarization135.tif";

    final private File _pol0File;
    final private File _pol45File;
    final private File _pol90File;
    final private File _pol135File;

    public TiffPolarizationImageFileSet(File rootFolder, ICapturedImageFileSet fileSet) {
        File parentFolder = formSetParentFolder(rootFolder, fileSet.getChannel(), fileSet.getSetName());

        if (!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        
        this._pol0File = new File(parentFolder, _pol0);
        this._pol45File = new File(parentFolder, _pol45);
        this._pol90File = new File(parentFolder, _pol90);
        this._pol135File = new File(parentFolder, _pol135);
    }

    @Override
    public File getFile(Polarization pol) {
        File file = null;

        switch (pol) {
            case pol0:
                file = _pol0File;
                break;

            case pol45:
                file = _pol45File;
                break;

            case pol90:
                file = _pol90File;
                break;

            case pol135:
                file = _pol135File;
                break;

            default:
                break;
        }

        return file;
    }

    /**
     * This method forms the parent folder for this set with the formula root/4Polar/polarizationImages/setName
     * 
     * @param rootFolder
     * @param channel
     * @param setName
     * @return
     */
    public static File formSetParentFolder(File rootFolder, int channel, String setName) {
        Path path = Paths.get(PathFactoryOfProject.getFolder_PolarizationImages(rootFolder).getAbsolutePath(),
                "Channel" + channel, setName);
        return path.toFile();
    }
}