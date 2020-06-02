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
    private static File _projectRootPath = null;

    /**
     * The folder that has all the imaging setup data, image information and etc.
     */
    private static String _folder_params = "0_Params";
    private static File _folder_paramsPath = null;

    /**
     * The folder that holds the polarization images of the project.
     */
    private static String _folder_polarizationImages = "2_PolarizationImages";
    private static File _folder_polarizationImagesPath = null;

    /**
     * The folder that holds the orientation images of the project.
     */
    private static String _folder_orientationImages = "3_OrientationImages";
    private static File _folder_orientationImagesPath = null;

    /**
     * The folder that holds the processed (bead) images of the project.
     */
    private static String _FOLDER_PROCCESSED_BEAD_IMAGES = "1_ProcessedBeadImages";
    private static File _FOLDER_PROCCESSED_BEAD_IMAGES_PATH = null;

    /**
     * Creates and returns the 4Polar folder inside root4PProject.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_4Polar(File root4PProject) {
        if (_projectRootPath != null) {
            return _projectRootPath;
        }

        Path path = Paths.get(root4PProject.getAbsolutePath(), _projectRoot);
        _projectRootPath = path.toFile();

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
        if (_folder_paramsPath != null) {
            return _folder_paramsPath;
        }

        Path path = Paths.get(getFolder_4Polar(root4PProject).getAbsolutePath(), _folder_params);
        _folder_paramsPath = path.toFile();

        if (!_folder_paramsPath.exists()) {
            _folder_paramsPath.mkdirs();
        }

        return _folder_paramsPath;
    }

    /**
     * Returns the folder that would contain the polarization images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_PolarizationImages(File root4PProject) {
        if (_folder_polarizationImagesPath != null) {
            return _folder_polarizationImagesPath;
        }

        _folder_polarizationImagesPath = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _folder_polarizationImages);

        if (!_folder_polarizationImagesPath.exists()) {
            _folder_polarizationImagesPath.mkdirs();
        }

        return _folder_polarizationImagesPath;
    }

    /**
     * Returns the folder that would contain the orientation images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_OrientationImages(File root4PProject) {
        if (_folder_orientationImagesPath != null) {
            return _folder_orientationImagesPath;
        }
        _folder_orientationImagesPath = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _folder_orientationImages);

        if (!_folder_orientationImagesPath.exists()) {
            _folder_orientationImagesPath.mkdirs();
        }

        return _folder_orientationImagesPath;
    }

    /**
     * Returns the folder that would contain the orientation images.
     * 
     * @param root4PProject is the location of the 4Polar folder.
     * @return
     */
    public static File getFolder_ProcessedBeadImages(File root4PProject) {
        if (_FOLDER_PROCCESSED_BEAD_IMAGES_PATH != null) {
            return _FOLDER_PROCCESSED_BEAD_IMAGES_PATH;
        }

        _FOLDER_PROCCESSED_BEAD_IMAGES_PATH = new File(getFolder_4Polar(root4PProject).getAbsolutePath(),
                _FOLDER_PROCCESSED_BEAD_IMAGES);

        if (!_FOLDER_PROCCESSED_BEAD_IMAGES_PATH.exists()) {
            _FOLDER_PROCCESSED_BEAD_IMAGES_PATH.mkdirs();
        }

        return _FOLDER_PROCCESSED_BEAD_IMAGES_PATH;
    }

}