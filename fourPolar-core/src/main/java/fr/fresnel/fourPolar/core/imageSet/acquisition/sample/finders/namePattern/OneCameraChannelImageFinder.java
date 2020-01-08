package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;

class OneCameraChannelImageFinder implements IChannelImageFinder {
    @Override
    public void find(File rootFolder, SampleImageSet sampleSet, int channel, String channelLabel, String[] polLabel, IImageChecker imageChecker) {
        File[] imagesPol0_45_90_135 = rootFolder.listFiles(new FilterCapturedImage(null, channelLabel, imageChecker.getExtension()));

        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            if (!imageChecker.checkCompatible(imagePol0_45_90_135))
                continue;

            CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_45_90_135);
            try {
                sampleSet.addImage(channel, fileSet);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    
}