package fr.fresnel.fourPolar.io.image.orientation.file;

import java.io.File;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * A concrete implementation of the {@link IOrientationImageFileSet}. Note that
 * with this implementation, all images will have a tif extension. The path for
 * angle images is as described in
 * {@link PathFactoryOfProject#getFolder_OrientationImages()} + [Rho, Delta,Eta]
 * + .tif
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

        File parentFolder = OrientationImageFileSetUtils.getParentFolder(root4PProject, channel, fileSet);

        _createParentFolder(parentFolder);

        this._rhoImage = _createRhoFile(parentFolder);
        this._deltaImage = _createDeltaFile(parentFolder);
        this._etaImage = _createEtaFile(parentFolder);
        this._channel = channel;
    }

    private void _createParentFolder(File parentFolder) {
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
    }

    private File _createEtaFile(File parentFolder) {
        return OrientationImageFileSetUtils.createEtaImageFile(parentFolder, _extension);
    }

    private File _createDeltaFile(File parentFolder) {
        return OrientationImageFileSetUtils.createDeltaImageFile(parentFolder, _extension);
    }

    private File _createRhoFile(File parentFolder) {
        return OrientationImageFileSetUtils.createRhoImageFile(parentFolder, _extension);
    }

    @Override
    public File getFile(OrientationAngle angle) {
        switch (angle) {
            case rho:
                return _rhoImage;

            case delta:
                return _deltaImage;

            case eta:
                return _etaImage;

            default:
                return null;
        }
    }

    @Override
    public int getChannel() {
        return this._channel;
    }

}