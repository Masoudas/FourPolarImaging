package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import javassist.tools.reflect.CannotCreateException;

public class CapturedImageFileSetBuilder implements ICapturedImageFileSetBuilder {
    final private Cameras _cameras;
    final private ICapturedImageChecker _checker;
    final private int _numChannels;
    final private boolean[] _buildChannels;

    /**
     * List of captured files, where the array length correspond to number of
     * camera, and the list keeps the images of channels for that camera.
     */
    final private List<ICapturedImageFile>[] _files;

    /**
     * @param imagingSetup is the corresponding imaging setup.
     * @param checker      is the set of constraints we put to accepted or reject a
     *                     given file as proper captured image.
     */
    @SuppressWarnings("unchecked")
    public CapturedImageFileSetBuilder(IFourPolarImagingSetup imagingSetup, ICapturedImageChecker checker) {
        this._checker = Objects.requireNonNull(checker, "checker cannot be null");
        Objects.requireNonNull(imagingSetup, "imaging setup cannot be null.");

        this._cameras = imagingSetup.getCameras();
        this._numChannels = imagingSetup.getNumChannel();
        this._files = new ArrayList[Cameras.getNImages(this._cameras)];
        this._buildChannels = new boolean[this._numChannels];

        for (int i = 0; i < this._files.length; i++) {
            this._files[i] = new ArrayList<>();
        }
    }

    @Override
    public CapturedImageFileSetBuilder add(int[] channels, File pol0_45_90_135) {
        _checkChannel(channels);
        if (this._cameras != Cameras.One) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0_45_90_135, "pol0_45_90_135 can't be null");

        IntStream.range(0, channels.length).forEach((i) -> {
            this._buildChannels[channels[i] - 1] = true;
        });
        this._files[0].add(new CapturedImageFile(channels, pol0_45_90_135));
        return this;
    }

    @Override
    public CapturedImageFileSetBuilder add(int[] channels, File pol0_90, File pol45_135) {
        _checkChannel(channels);
        if (this._cameras != Cameras.Two) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0_90, "pol0_90 can't be null");
        Objects.requireNonNull(pol45_135, "pol45_135 can't be null");

        IntStream.range(0, channels.length).forEach((i) -> {
            this._buildChannels[channels[i] - 1] = true;
        });
        this._files[0].add(new CapturedImageFile(channels, pol0_90));
        this._files[1].add(new CapturedImageFile(channels, pol45_135));
        return this;
    }

    @Override
    public CapturedImageFileSetBuilder add(int[] channels, File pol0, File pol45, File pol90, File pol135) {
        _checkChannel(channels);
        if (this._cameras != Cameras.Four) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0, "pol0 can't be null");
        Objects.requireNonNull(pol45, "pol45 can't be null");
        Objects.requireNonNull(pol90, "pol90 can't be null");
        Objects.requireNonNull(pol135, "pol135 can't be null");

        IntStream.range(0, channels.length).forEach((i) -> {
            this._buildChannels[channels[i] - 1] = true;
        });
        this._files[0].add(new CapturedImageFile(channels, pol0));
        this._files[1].add(new CapturedImageFile(channels, pol45));
        this._files[2].add(new CapturedImageFile(channels, pol90));
        this._files[3].add(new CapturedImageFile(channels, pol135));

        return this;
    }

    @Override
    public ICapturedImageFileSet build() throws CannotCreateException, IncompatibleCapturedImage {
        _checkAllChannelsAreGiven();
        _checkUsingChecker();

        ICapturedImageFileSet fileSet = _createFileSet();

        _resetBuilder();
        return fileSet;
    }

    private ICapturedImageFileSet _createFileSet() {
        ICapturedImageFileSet fileSet = null;
        if (this._cameras == Cameras.One) {
            fileSet = new CapturedImageFileSet(this._files[0].toArray(new ICapturedImageFile[0]));
        } else if (this._cameras == Cameras.Two) {
            fileSet = new CapturedImageFileSet(this._files[0].toArray(new ICapturedImageFile[0]),
                    this._files[1].toArray(new ICapturedImageFile[0]));
        } else if (this._cameras == Cameras.Four) {
            fileSet = new CapturedImageFileSet(this._files[0].toArray(new ICapturedImageFile[0]),
                    this._files[1].toArray(new ICapturedImageFile[0]),
                    this._files[2].toArray(new ICapturedImageFile[0]),
                    this._files[3].toArray(new ICapturedImageFile[0]));
        }
        return fileSet;
    }

    private void _checkUsingChecker() throws IncompatibleCapturedImage {
        for (List<ICapturedImageFile> cameraChannels : _files) {
            for (Iterator<ICapturedImageFile> channel = cameraChannels.iterator(); channel.hasNext();) {
                _checker.check(channel.next());
            }
        }
    }

    private void _checkAllChannelsAreGiven() throws CannotCreateException {
        if (IntStream.range(0, this._numChannels).anyMatch((i) -> !_buildChannels[i])) {
            throw new CannotCreateException("Not enough captured images are given for all channels.");
        }
    }

    private void _checkChannel(int[] channel) {
        IntSummaryStatistics stats = Arrays.stream(channel).summaryStatistics();
        if (stats.getMin() <= 0 || stats.getMax() > _numChannels) {
            throw new IllegalArgumentException(
                    "Channel must be greater than zero and less than total number of channels.");
        }
    }

    private void _resetBuilder() {
        IntStream.range(0, this._numChannels).forEach((i) -> _buildChannels[i] = false);
        for (List<ICapturedImageFile> cameraChannels : this._files) {
            cameraChannels.clear();
        }

    }

}