package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImageSet}.
 */
class PolarizationImageSet implements IPolarizationImageSet {
    final private IPolarizationImage _pol0;
    final private IPolarizationImage _pol45;
    final private IPolarizationImage _pol90;
    final private IPolarizationImage _pol135;
    final private ICapturedImageFileSet _fileSet;
    final private int _channel;

    /**
     * 
     * @param fileSet
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     * @throws CannotFormPolarizationImageSet
     */
    public PolarizationImageSet(IPolarizationImageSetBuilder builder) throws CannotFormPolarizationImageSet {
        this._pol0 = builder.getPol0();
        this._pol45 = builder.getPol45();
        this._pol90 = builder.getPol90();
        this._pol135 = builder.getPol135();
        this._fileSet = builder.getFileSet();
        this._channel = builder.getChannel();
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

    @Override
    public int channel() {
        return this._channel;
    }

}