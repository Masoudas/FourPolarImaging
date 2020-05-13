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
    private final static String _SoIImageName = "SoI.tif";

    private final File _SoIImageFile;
    private final int _channel;

    public TiffSoIImageFile(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        File parentFolder = TiffPolarizationImageFileSet.formSetParentFolder(root4PProject, channel,
                fileSet.getSetName());
        this._channel = channel;
        this._SoIImageFile = new File(parentFolder, _SoIImageName);
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