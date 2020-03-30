package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.DRectangle;

/**
 * Models the field of view of the bead images of a particular channel.
 */
public class FieldOfView implements IFieldOfView {
    private Hashtable<Polarization, DRectangle> _fov = new Hashtable<Polarization, DRectangle>(4);

    public FieldOfView(DRectangle pol0, DRectangle pol45, DRectangle pol90, DRectangle pol135){
        _fov.put(Polarization.pol0, pol0);
        _fov.put(Polarization.pol45, pol45);
        _fov.put(Polarization.pol90, pol90);
        _fov.put(Polarization.pol135, pol135);
    }

    @Override
    public DRectangle getFoV(Polarization pol) {
        return _fov.get(pol);
    }
    
}