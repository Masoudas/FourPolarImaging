package fr.fresnel.fourPolar.ui.algorithms.preprocess.soi;

import fr.fresnel.fourPolar.algorithm.preprocess.soi.ISoICalculator;
import fr.fresnel.fourPolar.algorithm.preprocess.soi.SoICalculator;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;

public class SoIImageCreator implements ISoIImageCreator {
    private final ISoICalculator[] _soiCalculators;

    public static ISoIImageCreator create(int numChannels) {
        return new SoIImageCreator(numChannels);
    }

    private SoIImageCreator(int numChannels) {
        this._soiCalculators = this._setSoICalculator(numChannels);

    }

    @Override
    public ISoIImage create(IPolarizationImageSet polarizationImageSet) {
        ISoIImage soiImage = this._createChannelSoIImages(polarizationImageSet);
        this._calculateChannelSoI(polarizationImageSet, soiImage);
        return soiImage;
    }

    /**
     * Set soi calculator.
     */
    private ISoICalculator[] _setSoICalculator(int numChannels) {
        ISoICalculator[] soiCalculators = new ISoICalculator[numChannels];
        for (int channel = 1; channel <= numChannels; channel++) {
            soiCalculators[channel - 1] = SoICalculator.create();
        }

        return soiCalculators;
    }

    private ISoIImage _createChannelSoIImages(IPolarizationImageSet polarizationImageSet) {
        return SoIImage.create(polarizationImageSet);

    }

    private void _calculateChannelSoI(IPolarizationImageSet polImageSet, ISoIImage soiImage) {
        int channel = polImageSet.channel();
        this._soiCalculators[channel - 1].calculateUINT16Sum(polImageSet.getIterator(),
                soiImage.getImage().getCursor());
    }

}