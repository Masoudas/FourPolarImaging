package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.Iterator;

class TwoCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public Iterator<File[]> find(SampleImageSetByNamePatternFinder sampleSetFinder, String channelLabel) {
        String[] polLabel = sampleSetFinder.getPolLabel();
        File[] imagesPol0_90 = sampleSetFinder.getRootFolder()
                .listFiles(new FilterCapturedImage(polLabel[0], channelLabel));

        MatchingFilesIterator itrCreator = new MatchingFilesIterator();
        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = sampleSetFinder.getRootFolder().listFiles(filterFile);

            if (candidatesPol45_135.length == 1) {
                itrCreator.add(new File[] { imagePol0_90, candidatesPol45_135[0] });
            }
        }

        return itrCreator.iterator();
    }
}