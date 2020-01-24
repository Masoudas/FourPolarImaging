package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class OneCameraChannelImageFinder implements IChannelImageFinder {
    private List<RejectedCapturedImage> _rejectedImages;

    @Override
    public List<RejectedCapturedImage> find(SampleImageSetByNamePatternFinder sampleSetFinder,
            SampleImageSet sampleImageSet, int channel, String channelLabel) throws NoImageFoundOnRoot {

        File[] imagesPol0_45_90_135 = sampleSetFinder.getRootFolder().listFiles(
                new FilterCapturedImage(null, channelLabel, sampleImageSet.getCapturedImageChecker().getExtension()));

        if (imagesPol0_45_90_135.length == 0) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        this._rejectedImages = new ArrayList<RejectedCapturedImage>();
        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            try {
                sampleImageSet.addImage(channel, imagePol0_45_90_135);

            } catch (CorruptCapturedImage e) {
                this._rejectedImages.add(e.getRejectedImage());
            }
        }

        if (sampleImageSet.getChannelImages(channel).isEmpty()) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        return this._rejectedImages;
    }

}