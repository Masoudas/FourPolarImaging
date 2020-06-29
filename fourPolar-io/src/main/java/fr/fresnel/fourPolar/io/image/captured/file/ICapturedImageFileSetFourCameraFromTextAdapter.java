package fr.fresnel.fourPolar.io.image.captured.file;

import java.io.File;
import java.util.Iterator;

import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;

class ICapturedImageFileSetFourCameraFromTextAdapter extends ICapturedImageFileSetFromTextAdapter {
    private static final int _POL0_START = ICapturedImageFileSetToTextAdapter._POL0_START;
    private static final int _POL45_START = ICapturedImageFileSetToTextAdapter._POL45_START;
    private static final int _POL90_START = ICapturedImageFileSetToTextAdapter._POL90_START;
    private static final int _POL135_START = ICapturedImageFileSetToTextAdapter._POL135_START;

    public ICapturedImageFileSetFourCameraFromTextAdapter(IFourPolarImagingSetup setup, ICapturedImageChecker checker,
            ICapturedImageFileSetBuilder builder) {
        super(setup, checker, builder);
    }

    public ICapturedImageFileSet fromString(Iterator<String[]> iterator, String setName)
            throws CorruptCapturedImageSet {
        return super.fromString(iterator, setName);
    }

    private File _pol0FromFileString(String pol0Line) throws IndexOutOfBoundsException {
        return new File(pol0Line.substring(_POL0_START));
    }

    private File _pol45FromFileString(String pol45Line) throws IndexOutOfBoundsException {
        return new File(pol45Line.substring(_POL45_START));
    }

    private File _pol90FromFileString(String pol90Line) throws IndexOutOfBoundsException {
        return new File(pol90Line.substring(_POL90_START));
    }

    private File _pol135FromFileString(String pol135Line) throws IndexOutOfBoundsException {
        return new File(pol135Line.substring(_POL135_START));
    }

    @Override
    protected void _addFilesToBuilder(String[] capturedImageGroup, String setName) throws CorruptCapturedImageSet {
        try {
            _builder.add(_channelsFromString(capturedImageGroup[0], setName), _pol0FromFileString(capturedImageGroup[1]),
                    _pol45FromFileString(capturedImageGroup[2]), _pol90FromFileString(capturedImageGroup[3]),
                    _pol135FromFileString(capturedImageGroup[4]));

        } catch (IndexOutOfBoundsException e) {
            throw new CorruptCapturedImageSet(setName);
        }

    }

}