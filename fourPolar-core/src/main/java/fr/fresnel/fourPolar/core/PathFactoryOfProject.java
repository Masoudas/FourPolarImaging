package fr.fresnel.fourPolar.core;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * By project we imply an analysis on a cetain number of images which are taken
 * using the same imaging setup parameters. This path factory generates all the
 * subfolders that are needed for accessing information of the project. Compare
 * to {@link PathFactoryOfGlobalInfo}. All these path are created once using the
 * root folder, and every time the same path is returned.
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
     * Creates and returns the project root folder.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_4Polar(File rootFolder) {
        if (_projectRootPath != null) {
            return _projectRootPath;
        }

        Path path = Paths.get(rootFolder.getAbsolutePath(), _projectRoot);
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
     * @param rootFolder
     * @return
     */
    public static File getFolder_Params(File rootFolder) {
        if (_folder_paramsPath != null) {
            return _folder_paramsPath;
        }

        Path path = Paths.get(getFolder_4Polar(rootFolder).getAbsolutePath(), _folder_params);
        File _folder_paramsPath = path.toFile();

        if (!_folder_paramsPath.exists()) {
            _folder_paramsPath.mkdirs();
        }

        return _folder_paramsPath;
    }

    /**
     * Returns the folder that would contain the polarization images.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_PolarizationImages(File rootFolder) {
        if (_folder_polarizationImagesPath != null) {
            return _folder_polarizationImagesPath;
        }

        File _folder_polarizationImagesPath = new File(getFolder_4Polar(rootFolder).getAbsolutePath(),
                _folder_polarizationImages);

        if (!_folder_polarizationImagesPath.exists()) {
            _folder_polarizationImagesPath.mkdirs();
        }

        return _folder_polarizationImagesPath;
    }

    /**
     * Returns the folder that would contain the polarization images.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_OrientationImages(File rootFolder) {
        if (_folder_orientationImagesPath != null) {
            return _folder_orientationImagesPath;
        }
        File _folder_orientationImagesPath = new File(getFolder_4Polar(rootFolder).getAbsolutePath(),
                _folder_orientationImages);

        if (!_folder_orientationImagesPath.exists()) {
            _folder_orientationImagesPath.mkdirs();
        }

        return _folder_orientationImagesPath;
    }

}