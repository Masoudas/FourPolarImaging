package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

class PolarizationImageComposite implements IPolarizationImageComposite {
    private final RegistrationRule _rule;
    private final Image<RGB16> _image;

    public PolarizationImageComposite(RegistrationRule rule, Image<RGB16> image) {
        this._rule = rule;
        this._image = image;
    }

    @Override
    public RegistrationRule getRegistrationRule() {
        return this._rule;
    }

    @Override
    public Image<RGB16> getImage() {
        return this._image;
    }
}