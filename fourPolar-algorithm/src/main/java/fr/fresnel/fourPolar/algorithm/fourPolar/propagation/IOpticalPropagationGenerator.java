package fr.fresnel.fourPolar.algorithm.fourPolar.propagation;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.CannotComputePropagationFactor;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
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
    public IOpticalPropagation generate(IChannel channel, INumericalAperture na)
            throws CannotComputePropagationFactor, OpticalPropagationNotInvertible;

} 