package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

class FourCameraChannelImageFinder implements IChannelImageFinder {
    private List<RejectedCapturedImage> _rejectedImages;

    @Override
    public List<RejectedCapturedImage> find(SampleImageSetByNamePatternFinder sampleSetFinder,
            SampleImageSet sampleImageSet, int channel, String channelLabel) throws NoImageFoundOnRoot {
        String[] polLabel = sampleSetFinder.getPolLabel();
        
        File[] imagesPol0 = sampleSetFinder.getRootFolder().listFiles(new FilterCapturedImage(polLabel[0], channelLabel,
                sampleImageSet.getCapturedImageChecker().getExtension()));

        if (imagesPol0.length == 0) {
            throw new NoImageFoundOnRoot("No images found for channel " + channel);
        }

        File[] polFiles = new File[4];
        this._rejectedImages = new ArrayList<RejectedCapturedImage>();
        for (File imagePol0 : imagesPol0) {
            polFiles[0] = imagePol0;

            int polCtr = 0;
            boolean polCandidateFound = true;
            while (++polCtr < 4 && polCandidateFound) {
                FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0, polLabel[0],
                        polLabel[polCtr]);
                File[] candidates = sampleSetFinder.getRootFolder().listFiles(filterFile);

                if (candidates.length != 1) {
                    polCandidateFound = false;
                    this._rejectedImages.add(
                            new RejectedCapturedImage(polFiles[0], "Other polarization images could no be detected."));
                } else {
                    polFiles[polCtr] = candidates[0];
                }
            }

            if (polCtr == 4) {
                try {
                    CapturedImageFileSet fileSet = new CapturedImageFileSet(polFiles[0], polFiles[1], polFiles[2],
                            polFiles[3]);
                    sampleImageSet.addImage(channel, fileSet);

                } catch (CorruptCapturedImage e) {
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