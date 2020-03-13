package fr.fresnel.fourPolar.core.image.polarization;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationsIntensitySet}. In this
 * implementation, we assume that the underlying intensity set is represented by
 * an intensity image.
 */
public class PolarizationImageSet implements IPolarizationImageSet {
    final private IPolarizationImage _pol0;
    final private IPolarizationImage _pol45;
    final private IPolarizationImage _pol90;
    final private IPolarizationImage _pol135;
    final private ICapturedImageFileSet _fileSet;

    /**
     * 
     * @param fileSet
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     * @throws CannotFormPolarizationImageSet
     */
    public PolarizationImageSet(ICapturedImageFileSet fileSet, Image<UINT16> pol0, Image<UINT16> pol45,
            Image<UINT16> pol90, Image<UINT16> pol135) throws CannotFormPolarizationImageSet {
        if (this._hasDuplicateImage(pol0, pol45, pol90, pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set due to duplicate image for polarizations.");
        }
        if (!this._hasEqualDimensions(pol0, pol45, pol90, pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set because the given images don't have the same dimension.");
        }

        this._pol0 = new PolarizationImage(Polarization.pol0, pol0);
        this._pol45 = new PolarizationImage(Polarization.pol45, pol45);
        this._pol90 = new PolarizationImage(Polarization.pol90, pol90);
        this._pol135 = new PolarizationImage(Polarization.pol135, pol135);
        this._fileSet = fileSet;
    }

    @Override
    public IIntensityVectorIterator getIterator() {
        return new IntensityVectorIterator(_pol0.getImage().getCursor(), _pol45.getImage().getCursor(),
                _pol90.getImage().getCursor(), _pol135.getImage().getCursor());
    }

    @Override
    public IPolarizationImage getPolarizationImage(Polarization pol) {
        IPolarizationImage image = null;

        switch (pol) {
            case pol0:
                image = _pol0;
                break;

            case pol45:
                image = _pol45;
                break;

            case pol90:
                image = _pol90;
                break;

            case pol135:
                image = _pol135;
                break;

            default:
                break;
        }

        return image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    /**
     * Checks that all images have the same dimension.
     */
    private boolean _hasEqualDimensions(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90,
            Image<UINT16> pol135) {
        return Arrays.equals(pol0.getDimensions(), pol45.getDimensions())
                && Arrays.equals(pol0.getDimensions(), pol90.getDimensions())
                && Arrays.equals(pol0.getDimensions(), pol135.getDimensions());
    }

    /**
     * checks for duplicate image reference.
     */
    private boolean _hasDuplicateImage(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90,
            Image<UINT16> pol135) {
        return pol0 == pol45 || pol0 == pol90 || pol0 == pol135 || pol45 == pol90 || pol45 == pol135 || pol90 == pol135;
    }

}