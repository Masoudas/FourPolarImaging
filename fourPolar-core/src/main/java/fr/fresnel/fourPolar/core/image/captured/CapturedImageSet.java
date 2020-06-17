package fr.fresnel.fourPolar.core.image.captured;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

class CapturedImageSet implements ICapturedImageSet {
    final private Map<String, ICapturedImage[]> _images;
    final private ICapturedImageFileSet _fileSet;
    final private boolean _hasMultiChannelImage;
    final private int _numChannels;

    public CapturedImageSet(ICapturedImageSetBuilder builder) {
        this._fileSet = builder.getFileSet();
        this._images = this._setImages(builder);
        this._hasMultiChannelImage = this._multiChannelImageExists();
        this._numChannels = _getNumChannels();
    }

    private int _getNumChannels() {
        return Arrays.stream(this._fileSet.getChannels()).max().getAsInt();
    }

    private Map<String, ICapturedImage[]> _setImages(ICapturedImageSetBuilder builder) {
        Hashtable<String, ICapturedImage[]> images = new Hashtable<>();
        for (String label : Cameras.getLabels(this._fileSet.getnCameras())) {
            images.put(label, builder.getCapturedImages(label));
        }

        return images;
    }

    private boolean _multiChannelImageExists() {
        boolean hasMultiChannelImage = false;
        for (Iterator<ICapturedImageFile> iterator = this._fileSet.getIterator(); iterator.hasNext()
                && !hasMultiChannelImage;) {
            hasMultiChannelImage = iterator.next().channels().length > 1;
        }

        return hasMultiChannelImage;
    }

    @Override
    public ICapturedImage[] getCapturedImage(String label) {
        return _images.get(label);
    }

    @Override
    public ICapturedImageFileSet fileSet() {
        return this._fileSet;
    }

    @Override
    public boolean hasMultiChannelImage() {
        return this._hasMultiChannelImage;
    }

    @Override
    public ICapturedImage getChannelPolarizationImage(int channel, Polarization polarization) {
        Objects.requireNonNull(polarization, "polarization can't be null");
        ChannelUtils.checkChannel(channel, this._numChannels);

        ICapturedImage[] polImages = _getPolarizationCapturedImages(polarization);
        
        ICapturedImage polImage = null;
        boolean imageFound = false;
        for ( int index = 0; index < polImages.length && !imageFound; index++ ) {
            if (this._capturedImageHasChannel(polImages[index], channel)) {
                polImage = polImages[index] ;
            }
        }

        return polImage;
    }

    private ICapturedImage[] _getPolarizationCapturedImages(Polarization polarization) {
        String label = Cameras.getLabelThatContainsPolarization(this._fileSet.getnCameras(), polarization);
        ICapturedImage[] polImages = this._images.get(label);
        return polImages;
    }

    private boolean _capturedImageHasChannel(ICapturedImage capturedImage, int channel) {
        return Arrays.stream(capturedImage.channels()).anyMatch((i) -> i == channel);
    }
}