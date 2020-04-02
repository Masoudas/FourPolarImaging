package fr.fresnel.fourPolar.io.image.polarization.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationFileSetTest {
    int _channel = 1;
    File _root = new File("/root");
    File _rootPol = PathFactoryOfProject.getFolder_PolarizationImages(_root);

    private File formSetParentFolder(String setName) {
        Path path = Paths.get(_rootPol.getAbsolutePath(), "Channel" + _channel, setName);
        return path.toFile();
    }

    @Test
    public void getFile_OneCameraCase_CreatesFilesInCorrectPath(){
        File pol0_45_90_135 = new File(_root, "pol0_45_90_135.tif");
        
        ICapturedImageFileSet fileSet = new CapturedImageFileSet(_channel, pol0_45_90_135);
        IPolarizationImageFileSet pfileSet = new TiffPolarizationImageFileSet(_root, fileSet);

        File parentFolder = formSetParentFolder(fileSet.getSetName());
        File expected_pol0 = new File(parentFolder, "pol0_45_90_135_p0.tif");
        File expected_pol45 = new File(parentFolder, "pol0_45_90_135_p45.tif");
        File expected_pol90 = new File(parentFolder, "pol0_45_90_135_p90.tif");
        File expected_pol135 = new File(parentFolder, "pol0_45_90_135_p135.tif");

        assertTrue(
            pfileSet.getFile(Polarization.pol0).getAbsolutePath().contentEquals(expected_pol0.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol45).getAbsolutePath().contentEquals(expected_pol45.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol90).getAbsolutePath().contentEquals(expected_pol90.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol135).getAbsolutePath().contentEquals(expected_pol135.getAbsolutePath())
        );
    }

    @Test
    public void getFile_TwoCameraCase_CreatesFilesInCorrectPath(){
        File pol0_90 = new File(_root, "pol0_90.tif");
        File pol45_135 = new File(_root, "pol45_135.tif");
        
        ICapturedImageFileSet fileSet = new CapturedImageFileSet(_channel, pol0_90, pol45_135);
        IPolarizationImageFileSet pfileSet = new TiffPolarizationImageFileSet(_root, fileSet);

        File parentFolder = formSetParentFolder(fileSet.getSetName());
        File expected_pol0 = new File(parentFolder, "pol0_90_p0.tif");
        File expected_pol45 = new File(parentFolder, "pol45_135_p45.tif");
        File expected_pol90 = new File(parentFolder, "pol0_90_p90.tif");
        File expected_pol135 = new File(parentFolder, "pol45_135_p135.tif");

        assertTrue(
            pfileSet.getFile(Polarization.pol0).getAbsolutePath().contentEquals(expected_pol0.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol45).getAbsolutePath().contentEquals(expected_pol45.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol90).getAbsolutePath().contentEquals(expected_pol90.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol135).getAbsolutePath().contentEquals(expected_pol135.getAbsolutePath())
        );
    }

    @Test
    public void getFile_FourCameraCase_CreatesFilesInCorrectPath(){
        File pol0 = new File(_root, "pol0.tif");
        File pol45 = new File(_root, "pol45.tif");
        File pol90 = new File(_root, "pol90.tif");
        File pol135 = new File(_root, "pol135.tif");
        
        ICapturedImageFileSet fileSet = new CapturedImageFileSet(_channel, pol0, pol45, pol90, pol135);
        IPolarizationImageFileSet pfileSet = new TiffPolarizationImageFileSet(_root, fileSet);

        File parentFolder = formSetParentFolder(fileSet.getSetName());
        File expected_pol0 = new File(parentFolder, "pol0.tif");
        File expected_pol45 = new File(parentFolder, "pol45.tif");
        File expected_pol90 = new File(parentFolder, "pol90.tif");
        File expected_pol135 = new File(parentFolder, "pol135.tif");

        assertTrue(
            pfileSet.getFile(Polarization.pol0).getAbsolutePath().contentEquals(expected_pol0.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol45).getAbsolutePath().contentEquals(expected_pol45.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol90).getAbsolutePath().contentEquals(expected_pol90.getAbsolutePath()) &&
            pfileSet.getFile(Polarization.pol135).getAbsolutePath().contentEquals(expected_pol135.getAbsolutePath())
        );
    }


}