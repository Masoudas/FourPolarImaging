package fr.fresnel.fourPolar.core.image.captured;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

class CapturedImageSet implements ICapturedImageSet {
    final private Map<String, ICapturedImage[]> _images;
    final private ICapturedImageFileSet _fileSet;
    final private boolean _hasMultiChannelImage;

    public CapturedImageSet(ICapturedImageSetBuilder builder) {
        this._fileSet = builder.getFileSet();
        this._images = this._setImages(builder);
        this._hasMultiChannelImage = this._multiChannelImageExists();
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

}