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

            _addFilesToBuilder(capturedImageGroup, setName);
        }

        try {
            return _buildCapturedImageSet(setName);
        } catch (CannotCreateException | IncompatibleCapturedImage e) {
            throw new CorruptCapturedImageSet(setName);
        }
    }

    private ICapturedImageFileSet _buildCapturedImageSet(String setName)
            throws CannotCreateException, IncompatibleCapturedImage, CorruptCapturedImageSet {
        ICapturedImageFileSet fileSet = _builder.build();
        _checkSetNameAfterBuild(setName, fileSet);
        return fileSet;
    }

    /**
     * Check the set name that is created by the builder correspond to the provided
     * set name and throw an exception otherwise.
     * 
     * @param setName is the supposed set name of this set.
     * @param fileSet is the file set built by the builder.
     * @throws CorruptCapturedImageSet
     */
    private void _checkSetNameAfterBuild(String setName, ICapturedImageFileSet fileSet) throws CorruptCapturedImageSet {
        if (setName.equals(fileSet.getSetName())) {
            throw new CorruptCapturedImageSet("The set name does not correspond to the provided name.");
        }
    }

    abstract protected void _addFilesToBuilder(String[] capturedImageGroupm, String setName)
            throws CorruptCapturedImageSet;

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