package fr.fresnel.fourPolar.io.image.captured.file;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;
import javassist.tools.reflect.CannotCreateException;

abstract class ICapturedImageFileSetFromTextAdapter {
    /**
     * The index where first channel number resides.
     */
    private static final int _CHANNEL_NO_START = ICapturedImageFileSetToTextAdapter._CHANNEL_NO_START;

    protected final CapturedImageFileSetBuilder _builder;

    private final int _nImages;

    protected ICapturedImageFileSetFromTextAdapter(IFourPolarImagingSetup setup, ICapturedImageChecker checker) {
        Objects.requireNonNull(setup, "setup can't be null");
        Objects.requireNonNull(checker, "checker can't be null");

        _builder = new CapturedImageFileSetBuilder(setup, checker);

        _nImages = Cameras.getNImages(setup.getCameras());
    }

    public ICapturedImageFileSet fromString(Iterator<String[]> iterator, String setName)
            throws CorruptCapturedImageSet {
        Objects.requireNonNull(iterator, "iterator can't be null");
        Objects.requireNonNull(setName, "setName can't be null");

        for (; iterator.hasNext();) {
            String[] capturedImageGroup = iterator.next();
            _checkGroupStringsLength(capturedImageGroup, setName);

            _addFilesToBuilder(capturedImageGroup);
        }

        try {
            return _builder.build();
        } catch (CannotCreateException | IncompatibleCapturedImage e) {
            throw new CorruptCapturedImageSet(setName);
        }
    }

    abstract protected void _addFilesToBuilder(String[] capturedImageGroup);

    protected int[] _channelsFromString(String channelLine) {
        String channelNoPortion = channelLine.substring(_CHANNEL_NO_START, channelLine.length() - 1);
        String[] channels = channelNoPortion.split(", ");

        return Stream.of(channels).mapToInt(Integer::parseInt).toArray();
    }

    private void _checkGroupStringsLength(String[] capturedImageGroup, String setName) throws CorruptCapturedImageSet {
        if (capturedImageGroup.length != _nImages + 1) {
            throw new CorruptCapturedImageSet(setName);
        }
    }

}