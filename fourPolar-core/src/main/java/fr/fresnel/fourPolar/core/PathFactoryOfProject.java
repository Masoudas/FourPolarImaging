package fr.fresnel.fourPolar.core;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * By project we imply an analysis on a group of {@link ICapturedImageFileSet}
 * of a particular {@link IFourPolarImagingSetup}.
 * <p>
 * The 4Polar is the root folder of the 4Polar project, where all the subsequent
 * folders and files of the project are stored. The location where this folder
 * is put is called root4PProject. The user determines the root4PProject.
 * <p>
 * This path factory generates all the subfolders that are needed for accessing
 * information of the project. Compare to {@link PathFactoryOfGlobalInfo}.
 */
public class PathFactoryOfProject {
    /**
     * Parent folder of all the project information. Every other subfolder is a
     * child of this folder.
     */
    private static String _projectRoot = "4Polar";

    /**
     * The folder that has all the imaging setup data, image information and etc.
     */
    private static String _folder_params = "0_Params";

    /**
     * The folder that holds the polarization images of the project.
     */
    private static String _folder_polarizationImages = "2_PolarizationImages";

    /**
     * The folder that holds the orientation images of the project.
     */
    private static String _folder_orientationImages = "3_OrientationImages";

    /**
     * The folder that holds the processed registration images of the project.
     */
    private static String _FOLDER_PROCCESSED_REGISTRATION_IMAGES = "1_ProcessedRegistrationImages";

    /**
     * The folder that holds the visualization sessions.
     */
    private static String _FOLDER_VISUALIZATION = "3_Visualization";

    /**
     * Creates and returns the 4Polar folder inside root4PProject.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_4Polar(File root4PProject) {
        Path path = Paths.get(root4PProject.getAbsolutePath(), _projectRoot);
        File _projectRootPath = path.toFile();

        if (!_projectRootPath.exists()) {
            _projectRootPath.mkdir();
        }

        return _projectRootPath;
    }

    /**
     * Returns the path to the params folder, and makes sure that the folder is
     * created.
     * <p>
     * The folder that has all the imaging setup data, image information and etc.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     */
    public static File getFolder_Params(File root4PProject) {
        Path path = Paths.get(getFolder_4Polar(root4PProject).getAbsolutePath(), _folder_params);
        File folder_paramsPath = path.toFile();

        if (!folder_paramsPath.exists()) {
            folder_paramsPath.mkdirs();
        }

        return folder_paramsPath;
    }

    /**
     * Returns the folder that would contain the polarization images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_PolarizationImages(File root4PProject) {
        File folder_polarizationImagesPath = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _folder_polarizationImages);

        if (!folder_polarizationImagesPath.exists()) {
            folder_polarizationImagesPath.mkdirs();
        }

        return folder_polarizationImagesPath;
    }

    /**
     * Returns the folder that would contain the orientation images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_OrientationImages(File root4PProject) {
        File folder_orientationImagesPath = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _folder_orientationImages);

        if (!folder_orientationImagesPath.exists()) {
            folder_orientationImagesPath.mkdirs();
        }

        return folder_orientationImagesPath;
    }

    /**
     * Returns the folder that would contain the orientation images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_ProcessedRegistrationImages(File root4PProject) {
        File folder_Processed_Registration_Images_Path = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _FOLDER_PROCCESSED_REGISTRATION_IMAGES);

        if (!folder_Processed_Registration_Images_Path.exists()) {
            folder_Processed_Registration_Images_Path.mkdirs();
        }

        return folder_Processed_Registration_Images_Path;
    }

    /**
     * Returns the folder that would contain the visualization sessions.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_Visualization(File root4PProject) {
        File folder_Visualization_Pth = new File(getFolder_4Polar(root4PProject).getAbsolutePath(), _FOLDER_VISUALIZATION);

        if (!folder_Visualization_Pth.exists()) {
            folder_Visualization_Pth.mkdirs();
        }

        return folder_Visualization_Pth;
    }

}