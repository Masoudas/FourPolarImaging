package fr.fresnel.fourPolar.io.image.captured.file;

import java.io.File;
import java.util.Iterator;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;
import javassist.tools.reflect.CannotCreateException;

class ICapturedImageFileSetOneCameraFromTextAdapter {
    /**
     * The index where first channel number resides.
     */
    private static final int _CHANNEL_NO_START = 12;

    /**
     * The index where first channel number resides.
     */
    private static final int _POL0_45_90_135_START = 12;

    private final CapturedImageFileSetBuilder _builder;

    public ICapturedImageFileSetOneCameraFromTextAdapter(IFourPolarImagingSetup setup) {
        _builder = new CapturedImageFileSetBuilder(setup, new CapturedImageExistsChecker());
    }

    public ICapturedImageFileSet fromString(Iterator<String[]> iterator, String setName)
            throws CorruptCapturedImageSet {
        for (; iterator.hasNext();) {
            String[] capturedImageGroup = iterator.next();
            _checkGroupStringsLength(capturedImageGroup, setName);

            _builder.add(_channelsFromString(capturedImageGroup[0]),
                    _pol0_45_90_135FromFileString(capturedImageGroup[1]));
        }

        try {
            return _builder.build();
        } catch (CannotCreateException | IncompatibleCapturedImage e) {
            throw new CorruptCapturedImageSet(setName);
        }
    }

    private int[] _channelsFromString(String channelLine) {
        String channelNoPortion = channelLine.substring(_CHANNEL_NO_START, channelLine.length() - 1);
        String[] channels = channelNoPortion.split(",");

        return Stream.of(channels).mapToInt(Integer::parseInt).toArray();
    }

    private File _pol0_45_90_135FromFileString(String pol0_45_90_135Line) {
        return new File(pol0_45_90_135Line.substring(_POL0_45_90_135_START));
    }

    private void _checkGroupStringsLength(String[] capturedImageGroup, String setName) throws CorruptCapturedImageSet {
        if (capturedImageGroup.length != 2) {
            throw new CorruptCapturedImageSet(setName);
        }
    }

}