package fr.fresnel.fourPolar.core.imagingSetup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.channel.Channel;

public class FourPolarImagingSetupTest {
    FourPolarImagingSetup setup = FourPolarImagingSetup.instance();

    @Test
    public void instance_setCompleteSet_CheckSetupImmutability() {
        Channel c1 = new Channel(1, 1, 1, 1, 1);
        Channel c2 = new Channel(2, 2, 2, 2, 2);

        setup.setCameras(Cameras.One);
        setup.setChannel(1, c1);
        setup.setChannel(2, c2);

        assertTrue(setup.getCameras() == Cameras.One && setup.getChannel(1).equals(c1) && setup.getChannel(2).equals(c2)
                && setup.getNumChannel() == 2);

        // Adding duplicate channels.
        Channel duplicateChannelNum = new Channel(3, 3, 3, 3, 3);
        Channel duplicateChannel = new Channel(1, 1, 1, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            setup.setChannel(2, duplicateChannelNum);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            setup.setChannel(3, duplicateChannel);
        });

    }

}