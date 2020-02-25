package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.fileContainer.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImage}
 */
public class PolarizationImage implements IPolarizationImage{
    final private IPolarizationImageFileSet _fileSet;
    final private Polarization _pol;
    final private Image<UINT16> _image;

    /**
     * A concrete implementation of the {@link IPolarizationImage}
     * 
     * @param fileSet is the file set associated with this image.
     * @param pol is the polarization of this image.
     * @param image is the image interface of this polarization image.
     */
    public PolarizationImage(IPolarizationImageFileSet fileSet, Polarization pol, Image<UINT16> image) {
        this._fileSet = fileSet;
        this._image = image;
        this._pol = pol;
    }

    @Override
    public IPolarizationImageFileSet getFileSet() {
        return this._fileSet;
    }

    @Override
    public Polarization getPolarization() {
        return this._pol;
    }

    @Override
    public Image<UINT16> getImage() {
        return this._image;
    }



}