package fr.fresnel.fourPolar.algorithm.fourPolar.propagation;

import fr.fresnel.fourPolar.algorithm.fourPolar.exceptions.fourPolar.propagation.CannotComputePropagationFactor;
import fr.fresnel.fourPolar.algorithm.fourPolar.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * The interface for generating the optical propagation of a propagation
 * channel.
 */
public interface IOpticalPropagationGenerator {
    /**
     * Generate the optical propagation for the given propagation channel.
     * 
     * @param channel
     * @return
     * @throws CannotComputePropagationFactor
     * @throws OpticalPropagationNotInvertible
     */
    public IOpticalPropagation generate(IPropagationChannel channel)
            throws CannotComputePropagationFactor, OpticalPropagationNotInvertible;

}