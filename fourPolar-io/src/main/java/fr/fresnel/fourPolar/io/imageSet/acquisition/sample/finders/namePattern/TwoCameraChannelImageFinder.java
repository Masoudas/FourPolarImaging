package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class TwoCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel, String channelLabel) 
        throws NoImageFoundOnRoot {
        String[] polLabel = sampleSetFinder.getPolLabel();

        File[] imagesPol0_90 = sampleSetFinder.getRootFolder()
                .listFiles(new FilterCapturedImage(polLabel[0], channelLabel, sampleSetFinder.getImageChecker().getExtension()));

        if (imagesPol0_90.length == 0){
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = sampleSetFinder.getRootFolder().listFiles(filterFile);

            if (candidatesPol45_135.length != 1)
                continue;

            CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_90, candidatesPol45_135[0]);
            sampleImageSet.addImage(channel, fileSet);
            
        }
        
        if (sampleImageSet.getChannelImages(channel).isEmpty()){
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }
    }

}