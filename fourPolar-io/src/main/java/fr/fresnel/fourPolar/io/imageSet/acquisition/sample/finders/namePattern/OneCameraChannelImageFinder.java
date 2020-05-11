package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.Iterator;

class OneCameraChannelImageFinder implements IChannelImageFinder {
    @Override
    public Iterator<File[]> find(SampleImageSetByNamePatternFinder sampleSetFinder, String channelLabel) {
        MatchingFilesIterator itrCreator = new MatchingFilesIterator();

        File[] imagesPol0_45_90_135 = sampleSetFinder.getRootFolder()
                .listFiles(new FilterCapturedImage(null, channelLabel));

        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            itrCreator.add(new File[] { imagePol0_45_90_135 });

        }

        return itrCreator.iterator();
    }

}