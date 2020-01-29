package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Models the field of view of the bead images of a particular channel.
 */
public class FieldOfView implements IFieldOfView {
    private Hashtable<Polarization, Rectangle> _fov = new Hashtable<Polarization, Rectangle>(4);

    public FieldOfView(Rectangle pol0, Rectangle pol45, Rectangle pol90, Rectangle pol135){
        _fov.put(Polarization.pol0, pol0);
        _fov.put(Polarization.pol45, pol45);
        _fov.put(Polarization.pol90, pol90);
        _fov.put(Polarization.pol135, pol135);
    }

    @Override
    public Rectangle getFoV(Polarization pol) {
        return _fov.get(pol);
    }
    
}