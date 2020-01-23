package fr.fresnel.fourPolar.core.image.captured;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * This class holds a captured image as an unsigned short.
 */
public class CapturedImage implements ICapturedImage {
    private ICapturedImageFileSet _fileSet;
    private String _fileLabel;
    private Img<UnsignedShortType> _img;

    /**
     * This class holds a captured image as an unsigned short.
     * 
     * @param fileSet   : The file set this image belongs to.
     * @param fileLabel : The label of the image file in the file set (as defined in
     *                  {@link Cameras})
     * @param img       : The corresponding image.
     * 
     * @throws IllegalArgumentException
     */
    public CapturedImage(ICapturedImageFileSet fileSet, String fileLabel, Img<UnsignedShortType> img) {
        this._fileSet = fileSet;
        
        _checkfileLabel(fileSet);
        this._fileLabel = fileLabel;

        if (img == null){
            throw new IllegalArgumentException("Captured image cannot be instantiated with an empty image.");
        }
        this._img = img;
    }

    private void _checkfileLabel(ICapturedImageFileSet fileSet) throws IllegalArgumentException{
        String[] labels = Cameras.getLabels(fileSet.getnCameras());
        if (!Arrays.stream(labels).anyMatch("fileLabel"::equals)){
            throw new IllegalArgumentException("The given file label is not in the fileSet.");
        }
    }

    /**
     * Returns the corresponding file set.
     */
    @Override
    public ICapturedImageFileSet getCapturedFileSet() {
        return _fileSet;
    }

    /**
     * Returns the label associated with the image in the file set.
     */
    @Override
    public String getLabel() {
        return _fileLabel;
    }

    /**
     * Returns the image.
     */
    @Override
    public Img<UnsignedShortType> getImage() {
        return _img;
    }
}