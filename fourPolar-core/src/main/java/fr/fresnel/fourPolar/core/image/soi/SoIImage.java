package fr.fresnel.fourPolar.core.image.soi;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of {@link ISoIImage}.
 */
public class SoIImage implements ISoIImage {
    final private Image<UINT16> _image;
    final private ICapturedImageFileSet _fileSet;

    /**
     * Create an empty SoI image, whose dimensions are equal to
     * {@link IPolarizationsImageSet}, using the provided image factory.
     * 
     * @param polarizationImageSet is a polarization image set.
     * @param factory              is the desired image factory.
     */
    public SoIImage(IPolarizationImageSet polarizationImageSet, ImageFactory factory) {
        long[] dim = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim();
        this._image = factory.create(dim, UINT16.zero());

        this._fileSet = polarizationImageSet.getFileSet();
    }

    /**
     * This constructor is used for an already generated SoI image.
     * 
     * @param fileSet
     * @param image
     */
    public SoIImage(ICapturedImageFileSet fileSet, Image<UINT16> image) {
        this._fileSet = fileSet;
        this._image = image;
    }

    @Override
    public Image<UINT16> getImage() {
        return this._image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

}