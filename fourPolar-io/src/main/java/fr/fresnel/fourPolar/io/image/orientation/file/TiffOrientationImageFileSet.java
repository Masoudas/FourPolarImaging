package fr.fresnel.fourPolar.io.image.orientation.file;

import java.io.File;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * A concrete implementation of the {@link IOrientationImageFileSet}. Note that
 * with this implementation, all images will have a tif extension. The path for
 * angle images is as described in
 * {@link PathFactoryOfProject#getFolder_OrientationImages()} + [Rho, Delta,Eta] + .tif
 * 
 */
public class TiffOrientationImageFileSet implements IOrientationImageFileSet {
    private final static String _extension = "tif";
    private final String _setName;
    private final File _rhoImage;
    private final File _deltaImage;
    private final File _etaImage;

    private final int _channel;

    /**
     * A concrete implementation of the {@link IOrientationImageFileSet}. The path
     * for angle images is
     * 
     * @param fileSet
     */
    public TiffOrientationImageFileSet(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        this._setName = fileSet.getSetName();

        File parentFolder = this._getSetParentFolder(root4PProject, channel);

        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        this._rhoImage = new File(parentFolder, "Rho" + "." + _extension);
        this._deltaImage = new File(parentFolder, "Delta" + "." + _extension);
        this._etaImage = new File(parentFolder, "Eta" + "." + _extension);
        this._channel = channel;
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

    private File _getSetParentFolder(File root4PProject, int channel) {
        return Paths.get(PathFactoryOfProject.getFolder_OrientationImages(root4PProject).getAbsolutePath(),
                "Channel " + channel, this._setName).toFile();
    }

    @Override
    public int getChannel() {
        return this._channel;
    }

}