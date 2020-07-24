package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.shape.IBoxShape;

/**
 * Models the field of view of the bead images of a particular channel.
 */
public class FieldOfView implements IFieldOfView {
    private Hashtable<Polarization, IBoxShape> _fov = new Hashtable<Polarization, IBoxShape>(4);

    public FieldOfView(IBoxShape pol0, IBoxShape pol45, IBoxShape pol90, IBoxShape pol135) {
        _checkPositive(pol0);
        _checkPositive(pol45);
        _checkPositive(pol90);
        _checkPositive(pol135);

        _fov.put(Polarization.pol0, pol0);
        _fov.put(Polarization.pol45, pol45);
        _fov.put(Polarization.pol90, pol90);
        _fov.put(Polarization.pol135, pol135);
    }

    @Override
    public IBoxShape getFoV(Polarization pol) {
        return _fov.get(pol);
    }

    /**
     * Check to make sure that pixels are positive.
     */
    private void _checkPositive(IBoxShape pol) {
        if (Arrays.stream(pol.min()).summaryStatistics().getMin() < 0) {
            throw new IllegalArgumentException("Pixels must be positive");
        }

    }

    @Override
    public long[] getMaximumLength() {
        return new long[] { this._calculateMaxFoVOnDim(0), this._calculateMaxFoVOnDim(1) };
    }

    private long _calculateMaxFoVOnDim(int dim) {
        ArrayList<Long> lensOnDim = new ArrayList<>();
        for (Polarization polarization : Polarization.values()) {
            IBoxShape polFoV = this.getFoV(polarization);
            lensOnDim.add(IBoxShape.len(polFoV, dim));
        }

        return Collections.max(lensOnDim);
    }

}