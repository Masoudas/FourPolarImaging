package fr.fresnel.fourPolar.io.image.polarization.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationFileSetTest {
    int _channel = 1;
    File _root = new File("/root");
    File _rootPol = PathFactoryOfProject.getFolder_PolarizationImages(_root);

    @Test
    public void getFile_OneCameraCase_CreatesFilesInCorrectPath() {
        int channel = 1;
        String setName_alias = "pol0_45_90_135";

        ICapturedImageFileSet fileSet = new DummyCapturedImageFileSet(setName_alias);
        IPolarizationImageFileSet pfileSet = new TiffPolarizationImageFileSet(_root, fileSet, channel);

        assertTrue(_checkParentFolderEqual(pfileSet, Polarization.pol0, Polarization.pol45)
                && _checkParentFolderEqual(pfileSet, Polarization.pol0, Polarization.pol90)
                && _checkParentFolderEqual(pfileSet, Polarization.pol0, Polarization.pol135));

        assertTrue(_checkExtensionTiff(pfileSet, Polarization.pol0) && _checkExtensionTiff(pfileSet, Polarization.pol45)
                && _checkExtensionTiff(pfileSet, Polarization.pol90)
                && _checkExtensionTiff(pfileSet, Polarization.pol135));
    }

    private boolean _checkParentFolderEqual(IPolarizationImageFileSet pfileSet, Polarization pol1, Polarization pol2) {
        return pfileSet.getFile(pol1).getParentFile().equals(pfileSet.getFile(pol2).getParentFile());
    }

    private boolean _checkExtensionTiff(IPolarizationImageFileSet pfileSet, Polarization pol1) {
        int last = pfileSet.getFile(pol1).getPath().lastIndexOf(".");
        return pfileSet.getFile(pol1).getPath().substring(last + 1).equals("tif");
    }

}

class DummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public DummyCapturedImageFileSet(String setName) {
        this.setName = setName;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return this.setName;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}
