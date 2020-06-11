package fr.fresnel.fourPolar.io.image.polarization.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

public class PolarizationImageFileSetUtils {
	public PolarizationImageFileSetUtils() {
		throw new AssertionError();
	}

	/**
	 * Creates the parent folder for where this set is stored with the formula root
	 * + {@link PathFactoryOfProject#getFolder_PolarizationImages(File)} + setName
	 * 
	 * @return
	 */
	public static File formSetParentFolder(File root4PProject, int channel, ICapturedImageFileSet fileSet) {
		Path path = Paths.get(PathFactoryOfProject.getFolder_PolarizationImages(root4PProject).getAbsolutePath(),
				"Channel " + channel, fileSet.getSetName());
		return path.toFile();
	}

	/**
	 * Create pol0 file.
	 * 
	 * @param setParentFolder is the folder where the polarization images to be
	 *                        stored.
	 * @param extension       is the desired extension.
	 * @return a file instance
	 */
	static File createPol0File(File setParentFolder, String extension) {
		return new File(setParentFolder, "Polarization_0." + extension);
	}

	/**
	 * Create pol45 file.
	 * 
	 * @param setParentFolder is the folder where the polarization images to be
	 *                        stored.
	 * @param extension       is the desired extension.
	 * @return a file instance
	 */
	static File createPol45File(File setParentFolder, String extension) {
		return new File(setParentFolder, "Polarization_45." + extension);
	}

	/**
	 * Create pol90 file.
	 * 
	 * @param setParentFolder is the folder where the polarization images to be
	 *                        stored.
	 * @param extension       is the desired extension.
	 * @return a file instance
	 */
	static File createPol90File(File setParentFolder, String extension) {
		return new File(setParentFolder, "Polarization_90." + extension);
	}

	/**
	 * Create pol135 file.
	 * 
	 * @param setParentFolder is the folder where the polarization images to be
	 *                        stored.
	 * @param extension       is the desired extension.
	 * @return a file instance
	 */
	static File createPol135File(File setParentFolder, String extension) {
		return new File(setParentFolder, "Polarization_135." + extension);
	}

}