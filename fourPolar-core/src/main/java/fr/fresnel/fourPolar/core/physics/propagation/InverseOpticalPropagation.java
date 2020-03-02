package fr.fresnel.fourPolar.core.physics.propagation;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This class models the inverse optical propagation as a matrix. Access to the the
 * propagation factors happen via the {@link IInverseOpticalPropagation}.
 */
public class InverseOpticalPropagation implements IInverseOpticalPropagation {
    final private Hashtable<String, Double> _inversePropagationFactors;
    final private IOpticalPropagation _opticalPropagation;
    
    public InverseOpticalPropagation(IOpticalPropagation opticalPropagation) {
        this._opticalPropagation = opticalPropagation;
        this._inversePropagationFactors = new Hashtable<String, Double>(16);
    }

    @Override
    public double getInverseFactor(Polarization polarization, DipoleSquaredComponent direction) {
        return _inversePropagationFactors.get(direction.toString() + polarization.toString());
    }

    @Override
    public void setInverseFactor(Polarization polarization, DipoleSquaredComponent direction, double factor) {
        _inversePropagationFactors.put(direction.toString() + polarization.toString(), factor);
    }

    @Override
    public IOpticalPropagation getOpticalPropagation() {
        return this._opticalPropagation;
    }

}