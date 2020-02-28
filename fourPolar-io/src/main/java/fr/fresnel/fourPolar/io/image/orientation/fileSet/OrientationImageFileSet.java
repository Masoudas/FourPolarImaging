package fr.fresnel.fourPolar.io.image.orientation.fileSet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.fileContainer.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * A concrete implementation of the {@link IOrientationImageFileSet}. Note that
 * with this implementation, all images will have a tiff extension.
 */
public class OrientationImageFileSet implements IOrientationImageFileSet {
    private final static String _extension = "tif";
    private final String _setName;
    private final File _rhoImage;
    private final File _deltaImage;
    private final File _etaImage;

    /**
     * A concrete implementation of the {@link IOrientationImageFileSet}. The path
     * for angle images is
     * 
     * @param fileSet
     */
    public OrientationImageFileSet(File rootFolder, ICapturedImageFileSet fileSet) {
        this._setName = fileSet.getSetName();

        Path parentFolder = this._getSetParentFolder(rootFolder, fileSet.getChannel());
        this._rhoImage = new File(parentFolder.toFile(), "Rho" + "." + _extension);
        this._deltaImage = new File(parentFolder.toFile(), "Delta" + "." + _extension);
        this._etaImage = new File(parentFolder.toFile(), "Eta" + "." + _extension);
    }

    @Override
    public File getFile(OrientationAngle angle) {
        File imageFile = null;

        switch (angle) {
            case rho:
                imageFile = _rhoImage;
                break;

            case delta:
                imageFile = _deltaImage;
                break;

            case eta:
                imageFile = _etaImage;
                break;

            default:
                break;
        }
        return imageFile;
    }

    @Override
    public String getSetName() {
        return this._setName;
    }

    private Path _getSetParentFolder(File rootFolder, int channel) {
        return Paths.get(PathFactoryOfProject.getFolder_OrientationImages(rootFolder).getAbsolutePath(),
                "Channel" + channel, this._setName);
    }

}