package fr.fresnel.fourPolar.io.physics.propagation;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.exceptions.fourPolar.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.fourPolar.IOpticalPropagationDatabase;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This database is a json implementation of
 * {@link IOpticalPropagationDatabase}. Access to this database only happens
 * through the io class of the database. The JSON field of this class is direcly
 * filled with the io class.
 */
class OpticalPropagationJSONDataBase implements IOpticalPropagationDatabase {
    @JsonProperty
    private ArrayList<IOpticalPropagationJSONAdaptor> _adaptorList;

    @Override
    public IOpticalPropagation search(IPropagationChannel channel) throws PropagationChannelNotInDatabase {
        int adaptorCtr = 0;
        while (adaptorCtr < _adaptorList.size()
                && !_adaptorList.get(adaptorCtr).fromJSON().getPropagationChannel().equals(channel)) {
            adaptorCtr++;
        }

        if (adaptorCtr == _adaptorList.size()) {
            throw new PropagationChannelNotInDatabase();
        } else {
            return _adaptorList.get(adaptorCtr).fromJSON();
        }
    }

    @Override
    public void add(IPropagationChannel channel, IOpticalPropagation opticalPropagation) {
        IOpticalPropagationJSONAdaptor adaptor = new IOpticalPropagationJSONAdaptor();
        adaptor.toJSON(opticalPropagation);
        _adaptorList.add(adaptor);
    }

}