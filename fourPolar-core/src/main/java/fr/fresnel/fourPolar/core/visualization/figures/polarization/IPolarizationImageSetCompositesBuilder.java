package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

abstract class IPolarizationImageSetCompositesBuilder {
   abstract IPolarizationImageComposite getCompositeImage(RegistrationRule rule);
   abstract int getChannel();
   abstract ICapturedImageFileSet getFileSet();
}