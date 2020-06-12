package fr.fresnel.fourPolar.algorithm.fourPolar.propagation;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.CannotComputePropagationFactor;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
 
/**
 * This class generates the optical propagation for the given propagation
 * channel.
 */
public class OpticalPropagationGenerator implements IOpticalPropagationGenerator {

    @Override
    public IOpticalPropagation generate(IChannel channel, INumericalAperture na)
            throws CannotComputePropagationFactor, OpticalPropagationNotInvertible {
        return null;
    }

}