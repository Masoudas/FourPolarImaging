package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

abstract class IPolarizationImageSetCompositesBuilder {
    public abstract IPolarizationImageComposite getCompositeImage(RegistrationRule rule);

    public abstract int getChannel();

    public abstract ICapturedImageFileSet getFileSet();
}