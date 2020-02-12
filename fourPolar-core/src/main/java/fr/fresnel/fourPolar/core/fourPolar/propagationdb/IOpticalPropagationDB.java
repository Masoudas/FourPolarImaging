package fr.fresnel.fourPolar.core.fourPolar.propagationdb; 

import fr.fresnel.fourPolar.core.exceptions.fourPolar.propagationdb.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This interface is used for accessing the optical propagation database.
 */
public interface IOpticalPropagationDB {
    /**
     * Adds a new optical propagation to the database.
     * 
     * @param opticalPropagation
     */
    public void add(IOpticalPropagation opticalPropagation);

    /**
     * Looks for the optical propagation corresponding to the given channel.
     * 
     * @param channel
     * @return
     * @throws PropagationChannelNotInDatabase is thrown if there's no optical
     *                                         propagation for this propagation
     *                                         channel.
     */
    public IOpticalPropagation search(IChannel channel, INumericalAperture na) throws PropagationChannelNotInDatabase;

}