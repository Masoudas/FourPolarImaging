package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.fourPolar.propagationdb.IPolarizationsIntensityIterator;
import fr.fresnel.fourPolar.core.image.polarization.fileContainer.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationsIntensitySet}. In this implementation,
 * we assume that the underlying intensity set is represented by an intensity image.
 */
public class PolarizationImageSet implements IPolarizationImageSet{
    final private IPolarizationImage _pol0;
    final private IPolarizationImage _pol45;
    final private IPolarizationImage _pol90;
    final private IPolarizationImage _pol135;
    final private IPolarizationImageFileSet _fileSet;

    public PolarizationImageSet(
        IPolarizationImageFileSet fileSet, IPolarizationImage pol0,
        IPolarizationImage pol45, IPolarizationImage pol90, IPolarizationImage pol135) {
        this._pol0 = pol0;
        this._pol45 = pol45;
        this._pol90 = pol90;
        this._pol135 = pol135;
        this._fileSet = fileSet;
    }

    @Override
    public IPolarizationsIntensityIterator getCursor() {
        return new PolarizationsIntensityIterator(
            _pol0.getImage().getCursor(), _pol45.getImage().getCursor(),
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
    public IPolarizationImageFileSet getFileSet() {
        return this._fileSet;
    }

}