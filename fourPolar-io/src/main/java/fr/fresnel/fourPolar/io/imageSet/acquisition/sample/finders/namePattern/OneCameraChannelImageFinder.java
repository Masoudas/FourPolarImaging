package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class OneCameraChannelImageFinder implements IChannelImageFinder {
    @Override
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel,
            String channelLabel) throws NoImageFoundOnRoot {
        File[] imagesPol0_45_90_135 = sampleSetFinder.getRootFolder().listFiles(
                new FilterCapturedImage(null, channelLabel, sampleSetFinder.getImageChecker().getExtension()));

        if (imagesPol0_45_90_135.length == 0) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            try {
                if (sampleSetFinder.getImageChecker().checkCompatible(imagePol0_45_90_135)) {
                    CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_45_90_135);
                    sampleImageSet.addImage(channel, fileSet);
                } else {
                    addRejectedImage(sampleSetFinder, imagePol0_45_90_135,
                            sampleSetFinder.getImageChecker().getIncompatibilityMessage());
                }
            } catch (CorruptCapturedImage e) {
                addRejectedImage(sampleSetFinder, imagePol0_45_90_135, e.getMessage());
            }
        }
    }

    private void addRejectedImage(SampleImageSetByNamePatternFinder sampleSetFinder, File file, String message) {
        sampleSetFinder.getRejectedImages().put(file, message);
    }

}