package fr.fresnel.fourPolar.io.image.soi.file;

import java.io.File;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

/**
 * A concrete implementation of the {@link IPolarizationImageFileSet}, which
 * puts the SoI image in the same folder as parent folder of
 * {@link TiffPolarizationImageFileSet}.
 */
public class TiffSoIImageFile implements ISoIImageFile {
    private final static String _EXTENSION = "SoI.tif";

    private final File _SoIImageFile;
    private final int _channel;

    public TiffSoIImageFile(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        File parentFolder = this._formSetParentFolder(root4PProject, fileSet, channel);
        _createParentFolder(parentFolder);
        
        this._channel = channel;
        this._SoIImageFile = _getImageFile(parentFolder);
    }

    private File _getImageFile(File parentFolder) {
        return SoIImageFileUtils.createImageFile(parentFolder, _EXTENSION);
    }

    private File _formSetParentFolder(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        return SoIImageFileUtils.getPolarizationSetParentFolder(root4PProject, channel, fileSet);
    }

    private void _createParentFolder(File parentFolder) {
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
    }

    @Override
    public File getFile() {
        return this._SoIImageFile;
    }

    @Override
    public int channel() {
        return this._channel;
    }
}