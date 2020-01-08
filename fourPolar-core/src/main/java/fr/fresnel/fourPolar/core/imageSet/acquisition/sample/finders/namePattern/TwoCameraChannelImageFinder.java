package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;

class TwoCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public void find(File rootFolder, SampleImageSet sampleSet, int channel, String channelLabel, String[] polLabel, IImageChecker imageChecker) {
        File[] imagesPol0_90 = rootFolder.listFiles(new FilterCapturedImage(polLabel[0], channelLabel, imageChecker.getExtension()));

        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = rootFolder.listFiles(filterFile);

            if (candidatesPol45_135.length != 1) 
                continue;

            CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_90, candidatesPol45_135[0]);
            try {
                sampleSet.addImage(channel, fileSet);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        

    }

    
}