package fr.fresnel.fourPolar.io.image.orientation.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class TiffOrientationImageFileSetTest {
    File _root = new File("/root");

    @Test
    public void getFile_AnAliasSet_CreatesFilesInCorrectPath() {
        int channel = 1;
        String setName_alias = "pol0_45_90_135";

        ICapturedImageFileSet fileSet = new DummyCapturedImageFileSet(setName_alias);
        IOrientationImageFileSet oFileSet = new TiffOrientationImageFileSet(_root, fileSet, channel);

        assertTrue(_checkParentFolderEqual(oFileSet, OrientationAngle.rho, OrientationAngle.delta)
                && _checkParentFolderEqual(oFileSet, OrientationAngle.rho, OrientationAngle.eta));

        assertTrue(_checkExtensionTiff(oFileSet, OrientationAngle.rho)
                && _checkExtensionTiff(oFileSet, OrientationAngle.rho)
                && _checkExtensionTiff(oFileSet, OrientationAngle.rho));
    }

    private boolean _checkParentFolderEqual(IOrientationImageFileSet oFileSet, OrientationAngle angle1,
            OrientationAngle angle2) {
        return oFileSet.getFile(angle1).getParentFile().equals(oFileSet.getFile(angle2).getParentFile());
    }

    private boolean _checkExtensionTiff(IOrientationImageFileSet oFileSet, OrientationAngle angle1) {
        int last = oFileSet.getFile(angle1).getPath().lastIndexOf(".");
        return oFileSet.getFile(angle1).getPath().substring(last + 1).equals("tif");
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
