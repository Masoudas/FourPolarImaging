package fr.fresnel.fourPolar.core.image.polarization.fileContainer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.fileContainer.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImageFileSet} for
 * grouping a set of polariozation image files.
 */
public class PolarizationImageFileSet implements IPolarizationImageFileSet {
    final private static String _pol0Label = "_p0";
    final private static String _pol45Label = "_p45";
    final private static String _pol90Label = "_p90";
    final private static String _pol135Label = "_p135";

    final private String _nameExtract;

    private File _pol0File;
    private File _pol45File;
    private File _pol90File;
    private File _pol135File;

    public PolarizationImageFileSet(File rootFolder, int channel, ICapturedImageFileSet fileSet) {
        this._nameExtract = fileSet.getSetName();

        File setParentFolder = this._formSetParentFolder(rootFolder, channel, _nameExtract);

        String[] labels = Cameras.getLabels(fileSet.getnCameras());
        switch (fileSet.getnCameras()) {
            case One:
                this._setFileNames(setParentFolder, fileSet.getFile(labels[0]));
                break;

            case Two:
                this._setFileNames(setParentFolder, fileSet.getFile(labels[0]), fileSet.getFile(labels[1]));
                break;

            case Four:
                this._setFileNames(setParentFolder, fileSet.getFile(labels[0]), fileSet.getFile(labels[1]),
                        fileSet.getFile(labels[2]), fileSet.getFile(labels[3]));
                break;

            default:
                break;
        }
    }

    @Override
    public File getFile(Polarization pol) {
        File file = null;

        switch (pol) {
            case pol0:
                file = _pol0File;
                break;

            case pol45:
                file = _pol45File;
                break;

            case pol90:
                file = _pol90File;
                break;

            case pol135:
                file = _pol135File;
                break;

            default:
                break;
        }

        return file;
    }

    @Override
    public String getSetName() {
        return this._nameExtract;
    }

    /**
     * For the one camera case, add the polarization labels to the single file.
     * 
     * @param setParentFolder
     * @param pol0_45_90_135
     */
    private void _setFileNames(File setParentFolder, File pol0_45_90_135) {
        this._pol0File = new File(setParentFolder, this._addLabelToFileName(pol0_45_90_135, _pol0Label));
        this._pol45File = new File(setParentFolder, this._addLabelToFileName(pol0_45_90_135, _pol45Label));
        this._pol90File = new File(setParentFolder, this._addLabelToFileName(pol0_45_90_135, _pol90Label));
        this._pol135File = new File(setParentFolder, this._addLabelToFileName(pol0_45_90_135, _pol135Label));
    }

    /**
     * For the two camera case, add labels of 0 and 90 after the pol0_90 and the
     * labels of 45 and 135 after to the pol45_135.
     * 
     * @param setParentFolder the parent folder designated to this set.
     * @param pol0_90
     * @param pol45_135
     */
    private void _setFileNames(File setParentFolder, File pol0_90, File pol45_135) {
        this._pol0File = new File(setParentFolder, this._addLabelToFileName(pol0_90, _pol0Label));
        this._pol45File = new File(setParentFolder, this._addLabelToFileName(pol45_135, _pol45Label));
        this._pol90File = new File(setParentFolder, this._addLabelToFileName(pol0_90, _pol90Label));
        this._pol135File = new File(setParentFolder, this._addLabelToFileName(pol45_135, _pol135Label));
    }

    /**
     * For the four camera case, simply copy the file names.
     * 
     * @param setParentFolder the parent folder designated to this set.
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     */
    private void _setFileNames(File setParentFolder, File pol0, File pol45, File pol90, File pol135) {
        this._pol0File = new File(setParentFolder, pol0.getName());
        this._pol45File = new File(setParentFolder, pol45.getName());
        this._pol90File = new File(setParentFolder, pol90.getName());
        this._pol135File = new File(setParentFolder, pol135.getName());
    }

    /**
     * This method adds the given label to file name, right before the file
     * extension name.
     * 
     * @param file
     * @param label
     * @return
     */
    private String _addLabelToFileName(File file, String label) {
        int index = file.getName().lastIndexOf(".");
        return file.getName().subSequence(0, index) + label + file.getName().substring(index);
    }

    /**
     * This method forms the parent folder for this set with the formula root +
     * 4Polar + polarizationImages + setName
     * 
     * @param rootFolder
     * @param channel
     * @param setName
     * @return
     */
    private File _formSetParentFolder(File rootFolder, int channel, String setName) {
        Path path = Paths.get(PathFactoryOfProject.getFolder_PolarizationImages(rootFolder).getAbsolutePath(),
                "Channel" + channel, setName);
        return path.toFile();
    }
}