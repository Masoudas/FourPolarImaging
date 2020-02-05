package fr.fresnel.fourPolar.io.fourPolar.propagationDb;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import fr.fresnel.fourPolar.core.exceptions.fourPolar.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.fourPolar.opticalProp.IOpticalPropagationDB;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.io.physics.propagation.IOpticalPropagationJSONAdaptor;

/**
 * This database is a json implementation of
 * {@link IOpticalPropagationDatabase}. Access to this database only happens
 * through the io class of the database. The JSON field of this class is direcly
 * filled with the io class.
 */
class XMLOpticalPropagationDB implements IOpticalPropagationDB {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("OpticalPropagation")
    private ArrayList<IOpticalPropagationJSONAdaptor> _adaptorList;

    public XMLOpticalPropagationDB() {
        _adaptorList = new ArrayList<IOpticalPropagationJSONAdaptor>();
    }

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