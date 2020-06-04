package fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff;

import java.io.File;
import java.nio.file.Paths;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Utility methods.
 */
class TiffPolarizationImageSetCompositesUtil {
    /**
     * This is the name of the folder inside a visualization session, where the
     * composite images will be stored.
     */
    private final static String _FOLDER_POLARIZATION_COMPOSITION_FIGURES = "PolarizationCompositionFigures";

    public static File getRuleFile(File rootCompositeImages, RegistrationRule rule) {
        return new File(rootCompositeImages, rule.toString() + ".tif");
    }

    /**
     * Return root folder for (non-registration) polarization images.
     */
    public static File getRootFolder(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet fileSet) {
        File visualizationFolder = PathFactoryOfProject.getFolder_Visualization(root4PProject);

        File rootFolder = Paths.get(visualizationFolder.getAbsolutePath(), visualizationSession,
                _FOLDER_POLARIZATION_COMPOSITION_FIGURES, ChannelUtils.channelAsString(channel), fileSet.getSetName())
                .toFile();

        if (!rootFolder.exists())
            rootFolder.mkdirs();

        return rootFolder;
    }

    /**
     * Return root folder for registration images.
     */
    public static File getRootFolderRegistrationComposites(File root4PProject, int channel) {
        File rootOfChannel = new File(PathFactoryOfProject.getFolder_ProcessedRegistrationImages(root4PProject),
                ChannelUtils.channelAsString(channel));

        if (!rootOfChannel.exists())
            rootOfChannel.mkdirs();

        return rootOfChannel;
    }
}