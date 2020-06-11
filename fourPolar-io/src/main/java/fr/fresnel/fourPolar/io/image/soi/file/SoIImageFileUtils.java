package fr.fresnel.fourPolar.io.image.soi.file;

import java.io.File;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.polarization.file.PolarizationImageFileSetUtils;

class SoIImageFileUtils {
    private SoIImageFileUtils(){
        throw new AssertionError();
    }    

    public static File getPolarizationSetParentFolder(File root4PProject, int channel, ICapturedImageFileSet fileSet) {
        return PolarizationImageFileSetUtils.formSetParentFolder(root4PProject, channel, fileSet);
    }

    public static File createImageFile(File parentFolder, String extension){
        return new File(parentFolder, "SumOfIntensity" + extension);
    }
}