package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class TwoCameraChannelImageFinder implements IChannelImageFinder {
    private List<RejectedCapturedImage> _rejectedImages;

    @Override
    public List<RejectedCapturedImage> find(SampleImageSetByNamePatternFinder sampleSetFinder,
            SampleImageSet sampleImageSet, int channel, String channelLabel) throws NoImageFoundOnRoot {
        String[] polLabel = sampleSetFinder.getPolLabel();

        File[] imagesPol0_90 = sampleSetFinder.getRootFolder().listFiles(new FilterCapturedImage(polLabel[0],
                channelLabel, sampleImageSet.getCapturedImageChecker().getExtension()));

        if (imagesPol0_90.length == 0) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        this._rejectedImages = new ArrayList<RejectedCapturedImage>();
        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = sampleSetFinder.getRootFolder().listFiles(filterFile);

            if (candidatesPol45_135.length != 1) {
                this._rejectedImages.add(
                        new RejectedCapturedImage(imagePol0_90, "Other polarization images could no be detected."));
            } else {
                try {
                    sampleImageSet.addImage(channel, imagePol0_90, candidatesPol45_135[0]);
                } catch (IncompatibleCapturedImage e) {
                    this._rejectedImages.add(e.getRejectedImage());
                }
            }
        }

        if (sampleImageSet.getChannelImages(channel).isEmpty()) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        return this._rejectedImages;
    }
}