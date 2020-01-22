package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class TwoCameraChannelImageFinder implements IChannelImageFinder {

    @Override
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel,
            String channelLabel) throws NoImageFoundOnRoot {
        String[] polLabel = sampleSetFinder.getPolLabel();

        File[] imagesPol0_90 = sampleSetFinder.getRootFolder().listFiles(
                new FilterCapturedImage(polLabel[0], channelLabel, sampleSetFinder.getImageChecker().getExtension()));

        if (imagesPol0_90.length == 0) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = sampleSetFinder.getRootFolder().listFiles(filterFile);

            if (candidatesPol45_135.length != 1) {
                this._addRejectedImage(sampleSetFinder, imagePol0_90,
                        sampleSetFinder.getImageChecker().getIncompatibilityMessage());
                continue;
            }

            try {
                if (this._checkImagesCompatible(imagePol0_90, candidatesPol45_135[0],
                        sampleSetFinder.getImageChecker())) {
                    CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_90, candidatesPol45_135[0]);
                    sampleImageSet.addImage(channel, fileSet);
                } else {
                    this._addRejectedImage(sampleSetFinder, imagePol0_90,
                            sampleSetFinder.getImageChecker().getIncompatibilityMessage());
                    this._addRejectedImage(sampleSetFinder, candidatesPol45_135[0],
                            sampleSetFinder.getImageChecker().getIncompatibilityMessage());

                }
            } catch (CorruptCapturedImage e) {
                this._addRejectedImage(sampleSetFinder, imagePol0_90,
                        sampleSetFinder.getImageChecker().getIncompatibilityMessage());
                this._addRejectedImage(sampleSetFinder, candidatesPol45_135[0],
                        sampleSetFinder.getImageChecker().getIncompatibilityMessage());
            }

        }

        if (sampleImageSet.getChannelImages(channel).isEmpty()) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }
    }

    private boolean _checkImagesCompatible(File pol0_90, File pol45_135, ICapturedImageChecker imageChecker)
            throws CorruptCapturedImage {
        if (!imageChecker.checkCompatible(pol0_90) || imageChecker.checkCompatible(pol45_135)) {
            return false;
        } else {
            return true;
        }

    }

    private void _addRejectedImage(SampleImageSetByNamePatternFinder sampleSetFinder, File imageFile, String message) {
        sampleSetFinder.getRejectedImages().put(imageFile, message);
    }

}