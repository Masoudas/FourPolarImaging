package fr.fresnel.fourPolar.io.physics.propagation;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.fresnel.fourPolar.core.exceptions.fourPolar.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.fourPolar.opticalProp.IOpticalPropagationDB;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.io.PathFactoryOfProject;

/**
 * Using this class, we write the {@link IOpticalPropagation} of each channel to
 * the project folder for the user as JSON.
 */
public class OpticalPropagationToJSON {

    public void write(File rootFolder, FourPolarImagingSetup setup, IOpticalPropagationDB database)
            throws PropagationChannelNotInDatabase, IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (int channel = 0; channel < setup.getnChannel(); channel++) {
            IOpticalPropagationJSONAdaptor adaptor = getJSONAdaptor(setup, database, channel);

            File path = getFilePath(rootFolder, channel);

            mapper.writeValue(path, adaptor);
        }

    }

    /**
     * Returns the JSON adaptor for each optical propagation of the setup.
     * 
     * @param setup
     * @param database
     * @throws PropagationChannelNotInDatabase
     */
    private IOpticalPropagationJSONAdaptor getJSONAdaptor(FourPolarImagingSetup setup, IOpticalPropagationDB database,
            int channel) throws PropagationChannelNotInDatabase {
        IOpticalPropagation optProp = database.search(setup.getPropagationChannel(channel));

        IOpticalPropagationJSONAdaptor jsonAdaptor = new IOpticalPropagationJSONAdaptor();
        jsonAdaptor.toJSON(optProp);

        return jsonAdaptor;

    }

    private File getFilePath(File rootFolder, int channel) {
        return new File(PathFactoryOfProject.getFolder_0_Params(rootFolder),
                "OpticalPropagation_Channel" + channel + ".json");

    }

}