package fr.fresnel.fourPolar.core.image.captured;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class CapturedImageSetBuilder extends ICapturedImageSetBuilder {
    private final Map<String, List<ICapturedImage>> _images;
    private ICapturedImageFileSet _fileSet;

    public CapturedImageSetBuilder(Cameras cameras) {
        this._images = this._createImageTable(cameras);
        this._fileSet = null;

    }

    private Map<String, List<ICapturedImage>> _createImageTable(Cameras cameras) {
        Map<String, List<ICapturedImage>> table = new Hashtable<>();

        for (String label : Cameras.getLabels(cameras)) {
            table.put(label, new ArrayList<ICapturedImage>());
        }

        return table;
    }

    public CapturedImageSetBuilder setCapturedImage(String label, ICapturedImageFile capturedImageFile,
            Image<UINT16> image) {
        Objects.requireNonNull(this._fileSet, "A fileSet has to be provided for the builder.");
        if (!this._fileSet.hasLabel(label)) {
            throw new IllegalArgumentException("The given label does not belong to this camera");
        }

        this._images.get(label).add(new CapturedImage(capturedImageFile, label, image));
        return this;
    }

    public CapturedImageSetBuilder setFileSet(ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(fileSet, "fileSet can't be null");

        if (this._fileSet != null) {
            throw new IllegalArgumentException("Can't set new fileSet until the image set is built.");
        }
        
        this._fileSet = fileSet;
        return this;
    }

    @Override
    ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    @Override
    ICapturedImage[] getCapturedImages(String label) {
        return this._images.get(label).toArray(new ICapturedImage[0]);
    }

    public ICapturedImageSet build() {
        this._checkFileSetNotNull();
        this._checkAllImagesOfEachCameraIsGiven();

        ICapturedImageSet imageSet = new CapturedImageSet(this);
        this._resetBuilder();

        return imageSet;
    }

    private void _checkFileSetNotNull() {
        Objects.requireNonNull(this._fileSet, "A fileSet has to be provided for the builder.");
    }

    private boolean _checkAllImagesOfEachCameraIsGiven() {
        boolean allImagesAreGiven = true;
        for (String label : Cameras.getLabels(this._fileSet.getnCameras())) {
            allImagesAreGiven &= this._images.get(label).size() == this._fileSet.getFile(label).length;
        }

        return allImagesAreGiven;
    }

    private void _resetBuilder() {
        for (List<ICapturedImage> cameraImages : this._images.values()) {
            cameraImages.clear();
        }
        this._fileSet = null;
    }
}