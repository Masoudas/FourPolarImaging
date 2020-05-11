package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

class FourCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public Iterator<File[]> find(SampleImageSetByNamePatternFinder sampleSetFinder, String channelLabel) {
        String[] polLabel = sampleSetFinder.getPolLabel();

        File[] imagesPol0 = sampleSetFinder.getRootFolder().listFiles(new FilterCapturedImage(polLabel[0], channelLabel);


        File[] polFiles = new File[4];
        MatchingFilesIterator itrCreator = new MatchingFilesIterator();
        for (File imagePol0 : imagesPol0) {
            polFiles[0] = imagePol0;

            boolean allPolsFound = true;
            for (int polCtr = 0; polCtr < 4 && allPolsFound; polCtr++){
                FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0, polLabel[0],
                        polLabel[polCtr]);
                File[] candidates = sampleSetFinder.getRootFolder().listFiles(filterFile);

                if (candidates.length != 1) {
                    allPolsFound = false;
                } else {
                    polFiles[polCtr] = candidates[0];
                }
            }

            if (allPolsFound) {
                    itrCreator.add(new File[]{polFiles[0], polFiles[1], polFiles[2], polFiles[3]});
            }
        }

        return itrCreator.iterator();
    }
}