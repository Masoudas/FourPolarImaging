package fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff;

import java.io.File;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Utility methods among the IO classes of the type.
 */
class TiffRegistrationCompositeFiguresUtils {
    /**
     * Get the composite associated with this rule.
     */
    public static File getRuleFile(File root4PProject, int channel, RegistrationRule rule) {
        File rootOfChannel = new File(PathFactoryOfProject.getFolder_ProcessedRegistrationImages(root4PProject),
                "Channel" + channel);

        rootOfChannel.mkdir();
        return new File(rootOfChannel, rule.toString() + ".tif");
    }
}