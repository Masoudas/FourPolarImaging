package fr.fresnel.fourPolar.io.image.polarization.soi.file;

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

    public TiffSoIImageFile(File rootFolder, ICapturedImageFileSet fileSet) {
        File parentFolder = TiffPolarizationImageFileSet.formSetParentFolder(rootFolder, fileSet.getChannel(),
                fileSet.getSetName());

        this._SoIImageFile = new File(parentFolder, _SoIImageName);
    }

    @Override
    public File getFile() {
        return this._SoIImageFile;
    }

}