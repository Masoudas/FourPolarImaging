package fr.fresnel.fourPolar.core.physics.propagation;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This class models the optical propagation as a matrix. Access to the the
 * propagation factors happen via the {@link IOpticalPropagation}.
 */
public class OpticalPropagation implements IOpticalPropagation {
    final private Hashtable<String, Double> _propagationFactors;
    private IPropagationChannel _channel;

    public OpticalPropagation() {
        _propagationFactors = new Hashtable<String, Double>(16);
    }

    @Override
    public double getPropagationFactor(DipoleSquaredComponent direction, Polarization polarization) {
        return _propagationFactors.get(direction.toString() + polarization.toString());
    }

    @Override
    public void setPropagationFactor(DipoleSquaredComponent direction, Polarization polarization, double factor) {
        _propagationFactors.put(direction.toString() + polarization.toString(), factor);
    }

    @Override
    public IPropagationChannel getPropagationChannel() {
        return _channel;
    }

    @Override
    public void setPropagationChannel(IPropagationChannel channel) {
        this._channel = channel;
    }

}