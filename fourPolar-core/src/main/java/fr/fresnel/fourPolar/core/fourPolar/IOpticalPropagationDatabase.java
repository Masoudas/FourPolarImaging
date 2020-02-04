package fr.fresnel.fourPolar.core.fourPolar;

import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This interface is used for accessing the optical propagation database.
 * 
 */
public interface IOpticalPropagationDatabase {
    /**
     * Adds a new optical propagation to the database.
     * @param channel
     * @param opticalPropagation
     */
    public void add(IPropagationChannel channel, IOpticalPropagation opticalPropagation);

    /**
     * Looks for the optical propagation corresponding to the given channel.
     * @param channel
     * @return
     */
    public IOpticalPropagation search(IPropagationChannel channel);
    
}