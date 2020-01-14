package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Models the field of view of the bead images of a particular channel.
 */
class FieldOfView implements IFieldOfView {
    private Hashtable<Polarizations, Rectangle> _fov = new Hashtable<Polarizations, Rectangle>(4);

    public FieldOfView(Rectangle pol0, Rectangle pol45, Rectangle pol90, Rectangle pol135){
        _fov.put(Polarizations.pol0, pol0);
        _fov.put(Polarizations.pol45, pol45);
        _fov.put(Polarizations.pol90, pol90);
        _fov.put(Polarizations.pol135, pol135);
    }

    @Override
    public Rectangle getFoV(Polarizations pol) {
        return _fov.get(pol);
    }
    
}