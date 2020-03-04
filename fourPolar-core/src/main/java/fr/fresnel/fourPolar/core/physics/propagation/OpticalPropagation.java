package fr.fresnel.fourPolar.core.physics.propagation;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This class models the optical propagation as a matrix. Access to the the
 * propagation factors happen via the {@link IOpticalPropagation}.
 */
public class OpticalPropagation implements IOpticalPropagation {
    final private Hashtable<String, Double> _propagationFactors;
    final private IChannel _channel;
    final private INumericalAperture _na;

    public OpticalPropagation(IChannel channel, INumericalAperture na) {
        _channel = channel;
        _na = na;
        _propagationFactors = new Hashtable<String, Double>(16);
    }

    @Override
    public double getPropagationFactor(DipoleSquaredComponent component, Polarization polarization) throws PropagationFactorNotFound {
        try {
            return _propagationFactors.get(component.toString() + polarization.toString());
        } catch (NullPointerException e) {
            throw new PropagationFactorNotFound(component, polarization);
        }
    }

    @Override
    public void setPropagationFactor(DipoleSquaredComponent direction, Polarization polarization, double factor) {
        _propagationFactors.put(direction.toString() + polarization.toString(), factor);
    }

    @Override
    public IChannel getChannel() {
        return _channel;
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return _na;
    }


}