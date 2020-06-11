package fr.fresnel.fourPolar.io.image.polarization.file;

import java.io.File;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImageFileSet} for
 * grouping a set of polariozation image files.
 */
public class TiffPolarizationImageFileSet implements IPolarizationImageFileSet {
    final private static String _EXTENSION = "tif";

    final private File _pol0File;
    final private File _pol45File;
    final private File _pol90File;
    final private File _pol135File;

    public TiffPolarizationImageFileSet(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        File parentFolder = _createSetParentFolder(root4PProject, fileSet, channel);

        _checkParentFolderExists(parentFolder);

        this._pol0File = _createPol0File(parentFolder);
        this._pol45File = _createPol45File(parentFolder);
        this._pol90File = _createPol90File(parentFolder);
        this._pol135File = _createPol135File(parentFolder);
    }

    private File _createSetParentFolder(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        return PolarizationImageFileSetUtils.formSetParentFolder(root4PProject, channel, fileSet);
    }

    private File _createPol135File(File parentFolder) {
        return PolarizationImageFileSetUtils.createPol135File(parentFolder, _EXTENSION);
    }

    private File _createPol90File(File parentFolder) {
        return PolarizationImageFileSetUtils.createPol90File(parentFolder, _EXTENSION);
    }

    private File _createPol45File(File parentFolder) {
        return PolarizationImageFileSetUtils.createPol45File(parentFolder, _EXTENSION);
    }

    private File _createPol0File(File parentFolder) {
        return PolarizationImageFileSetUtils.createPol0File(parentFolder, _EXTENSION);
    }

    private void _checkParentFolderExists(File parentFolder) {
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
    }

    @Override
    public File getFile(Polarization pol) {
        switch (pol) {
            case pol0:
                return _pol0File;

            case pol45:
                return _pol45File;

            case pol90:
                return _pol90File;

            case pol135:
                return _pol135File;

            default:
                return null;
        }
    }
}