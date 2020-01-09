package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.io.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

class FourCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel, String channelLabel) {
        String[] polLabel = sampleSetFinder.getPolLabel();
        File[] imagesPol0 = sampleSetFinder.getRootFolder()
                .listFiles(new FilterCapturedImage(polLabel[0], channelLabel, sampleSetFinder.getImageChecker().getExtension()));

        File[] polFiles = new File[4];
        for (File imagePol0 : imagesPol0) {
            if (!sampleSetFinder.getImageChecker().checkCompatible(imagePol0))
                continue;

            polFiles[0] = imagePol0;
            for (int i = 1; i < polLabel.length; i++) {
                FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0, polLabel[0], polLabel[i]);
                File[] candidates = sampleSetFinder.getRootFolder().listFiles(filterFile);

                if (candidates.length != 1 || !sampleSetFinder.getImageChecker().checkCompatible(imagePol0))
                    break;

                polFiles[i] = candidates[0];
            }

            CapturedImageFileSet fileSet = new CapturedImageFileSet(polFiles[0], polFiles[1], polFiles[2], polFiles[3]);
            try {
                sampleImageSet.addImage(channel, fileSet);
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

}