package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

class PolarizationImageComposite implements IPolarizationImageComposite {
    private final RegistrationRule _rule;
    private final Image<ARGB8> _image;

    public PolarizationImageComposite(RegistrationRule rule, Image<ARGB8> image) {
        this._rule = rule;
        this._image = image;
    }

    @Override
    public RegistrationRule getRegistrationRule() {
        return this._rule;
    }

    @Override
    public Image<ARGB8> getImage() {
        return this._image;
    }
}