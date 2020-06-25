package fr.fresnel.fourPolar.io.image.captured.file;

import java.io.File;
import java.util.Iterator;

import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;

class ICapturedImageFileSetTwoCameraFromTextAdapter extends ICapturedImageFileSetFromTextAdapter {
    private static final int _POL0_90_START = ICapturedImageFileSetToTextAdapter._POL0_90_START;
    private static final int _POL45_135_START = ICapturedImageFileSetToTextAdapter._POL45_135_START;

    public ICapturedImageFileSetTwoCameraFromTextAdapter(IFourPolarImagingSetup setup, ICapturedImageChecker checker) {
        super(setup, checker);
    }

    public ICapturedImageFileSet fromString(Iterator<String[]> iterator, String setName)
            throws CorruptCapturedImageSet {
        return super.fromString(iterator, setName);
    }

    private File _pol0_90FromFileString(String pol0_90Line, String setName) throws IndexOutOfBoundsException {
        return new File(pol0_90Line.substring(_POL0_90_START));
    }

    private File _pol45_135FromFileString(String pol45_135Line, String setName) throws IndexOutOfBoundsException {
        return new File(pol45_135Line.substring(_POL45_135_START));
    }

    @Override
    protected void _addFilesToBuilder(String[] capturedImageGroup, String setName) throws CorruptCapturedImageSet {
        try {
            _builder.add(_channelsFromString(capturedImageGroup[0]),
                    _pol0_90FromFileString(capturedImageGroup[1], setName),
                    _pol45_135FromFileString(capturedImageGroup[1], setName));

        } catch (IndexOutOfBoundsException e) {
            throw new CorruptCapturedImageSet(setName);
        }

    }

}