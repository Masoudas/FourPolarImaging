package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.io.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

class OneCameraChannelImageFinder implements IChannelImageFinder {
    @Override
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel, String channelLabel) {
        File[] imagesPol0_45_90_135 = sampleSetFinder.getRootFolder()
                .listFiles(new FilterCapturedImage(null, channelLabel, sampleSetFinder.getImageChecker().getExtension()));

        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            if (!sampleSetFinder.getImageChecker().checkCompatible(imagePol0_45_90_135))
                continue;

            CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_45_90_135);
            try {
                sampleImageSet.addImage(channel, fileSet);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}