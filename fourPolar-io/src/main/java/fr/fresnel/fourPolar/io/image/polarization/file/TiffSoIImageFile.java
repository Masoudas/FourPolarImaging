package fr.fresnel.fourPolar.io.image.polarization.file;

import java.io.File;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImageFileSet}, which
 * puts the SoI image in the same folder as parent folder of
 * {@link TiffPolarizationImageFileSet}.
 */
public class TiffSoIImageFile implements ISoIImageFile {
    private final static String _SoIImageName = "SoI.tif";

    private final File _SoIImageFile;

    public TiffSoIImageFile(TiffPolarizationImageFileSet fileSet) {
        File parentFolder = fileSet.getFile(Polarization.pol0).getParentFile();

        this._SoIImageFile = new File(parentFolder, _SoIImageName);
    }

    @Override
    public File getFile() {
        return this._SoIImageFile;
    }

}