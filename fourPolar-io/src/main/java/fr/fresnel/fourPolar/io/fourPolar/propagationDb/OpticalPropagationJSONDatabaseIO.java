package fr.fresnel.fourPolar.io.fourPolar.propagationDb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.fresnel.fourPolar.core.fourPolar.propagationDb.IOpticalPropagationDatabase;
import fr.fresnel.fourPolar.io.PathFactoryOfGlobalInfo;

/**
 * This class is responsible for reading and writing the JSON implementation of
 * {@link IOpticalPropagationDatabase}
 */
public class OpticalPropagationJSONDatabaseIO {

    /**
     * Write the given {@link IOpticalPropagationDatabase} to the disk. the path to
     * which the base is written in the hidden folder of the project
     * (4PolarSoftware/Data).
     * 
     * @param path
     * @throws IOException
     */
    public void write(IOpticalPropagationDatabase database)
            throws IOException {
        if (!(database instanceof OpticalPropagationJSONDatabase)) {
            throw new IOException(
                    "The given database is not a JSON database, hence cannot be serialized with this class");
        }
        OpticalPropagationJSONDatabase jsonDatabase = (OpticalPropagationJSONDatabase) database;

        File path = getPath();
        if (path.exists()) {
            path.delete();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writeValue(getPath(), jsonDatabase);
    }

    /**
     * Reads the database from the disk. For this end, it checks the global
     * information folder first and if the db is not there, copies the original from
     * the resource folder, and then returns it to the user.
     * 
     * @throws IOException
     */
    public IOpticalPropagationDatabase read() throws IOException {
        File path = getPath();
        if (!path.exists()) {
            _copyOriginalDatabase();
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.getAbsolutePath(), OpticalPropagationJSONDatabase.class);
    }

    /**
     * Returns the complete path to the database.
     */
    private File getPath() {
        return new File(PathFactoryOfGlobalInfo.getFolder_Data(), "OpticalPropagationDB.json");
    }

    /**
     * This method copies the original database to the global information folder of
     * the project.
     * 
     * @throws IOException
     */
    private void _copyOriginalDatabase() throws IOException {
        Path originalDB = Paths
                .get(OpticalPropagationJSONDatabase.class.getResource("OpticalPropagationDB.json").toString());

        Path copyDB = Paths.get(getPath().getAbsolutePath());
        try {
            Files.copy(originalDB, copyDB, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            throw new IOException("Unable to copy the optical propagation db to the global info.");
        }

    }
}