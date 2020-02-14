package fr.fresnel.fourPolar.io.fourPolar.propagationdb;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import fr.fresnel.fourPolar.core.fourPolar.propagationdb.IOpticalPropagationDB;
import fr.fresnel.fourPolar.io.PathFactoryOfGlobalInfo;

/**
 * This class is responsible for reading and writing the JSON implementation of
 * {@link IOpticalPropagationDB}
 */
public class XMLOpticalPropagationDBIO {
    private static String folderName = "OpticalPropagation";
    private static String dbDiskName = "OpticalPropagationDB.xml";

    /**
     * Write the given {@link IOpticalPropagationDB} to the disk. the path to which
     * the base is written in the hidden folder of the project
     * (4PolarSoftware/Data).
     * 
     * @param path
     * @throws IOException
     */
    public void write(IOpticalPropagationDB database) throws IOException {
        if (!(database instanceof XMLOpticalPropagationDB)) {
            throw new IOException(
                    "The given database is not a JSON database, hence cannot be serialized with this class");
        }
        XMLOpticalPropagationDB xmlDatabase = (XMLOpticalPropagationDB) database;

        File path = _getDataBasePath();
        if (path.exists()) {
            path.delete();
        }

        ObjectMapper mapper = _getXMLMapper();
        mapper.writeValue(_getDataBasePath(), xmlDatabase);
    }

    /**
     * Creates the xml mapper object.
     * 
     * @return
     */
    private ObjectMapper _getXMLMapper() {
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);

        ObjectMapper mapper = new XmlMapper(xmlModule);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper;
    }

    /**
     * Reads the database from the disk. For this end, it checks the global
     * information folder first and if the db is not there, copies the original from
     * the resource folder, and then returns it to the user.
     * 
     * @throws IOException
     */
    public IOpticalPropagationDB read() throws IOException {
        File path = _getDataBasePath();
        if (!path.exists()) {
            _copyOriginalDatabase();
        }

        ObjectMapper mapper = _getXMLMapper();
        return mapper.readValue(path, XMLOpticalPropagationDB.class);
    }

    /**
     * Returns the complete path to the database.
     */
    private File _getDataBasePath() {
        Path path = Paths.get(PathFactoryOfGlobalInfo.getFolder_Data().getAbsolutePath(), folderName, dbDiskName);
        File pathAsFile = path.toFile();

        if (!pathAsFile.getParentFile().exists()) {
            pathAsFile.getParentFile().mkdirs();
        }

        return pathAsFile;
    }

    /**
     * This method copies the original database to the global information folder of
     * the project. The original database is stored in the resource folder of this class.
     * 
     * @throws IOException
     */
    private void _copyOriginalDatabase() throws IOException {
        URL resourcePath =  XMLOpticalPropagationDB.class.getResource(dbDiskName);
        if (resourcePath == null){
            throw new IOException("Original db was not found");
        }

        Path originalDB = Paths.get(resourcePath.getPath());

        Path copyDB = Paths.get(this._getDataBasePath().getAbsolutePath());
        try {
            Files.copy(originalDB, copyDB);
        } catch (IOException e) {
            throw new IOException("Unable to copy the optical propagation db to the global info folder.");
        }

    }
}
