package fr.fresnel.fourPolar.io.preprocess;

import java.io.File;

import fr.fresnel.fourPolar.io.PathFactoryOfProject;

class RegistrationSetProcessResultToYAMLFile {
    private static String _FILE_NAME = "Registration_Image_Set_Process_Results.yaml";

    /**
     * Creates a file that contains the full path to the file that holds the
     * registration result yaml, including the file itself.
     */
    public static File getPathToYaml(File root4PProject) {
        File rootFolder = PathFactoryOfProject.getFolder_Params(root4PProject);

        return new File(rootFolder, _FILE_NAME);

    }

}