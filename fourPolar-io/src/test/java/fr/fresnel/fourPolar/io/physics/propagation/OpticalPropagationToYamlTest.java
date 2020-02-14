package fr.fresnel.fourPolar.io.physics.propagation;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.fourPolar.propagationdb.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.fourPolar.propagationdb.IOpticalPropagationDB;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;
import fr.fresnel.fourPolar.io.fourPolar.propagationdb.XMLOpticalPropagationDBIO;

public class OpticalPropagationToYamlTest {
    @Test
    public void write_AddNewPropagationsToDB_WritesNewPropagationsInTheResourceFolder()
            throws IOException, PropagationChannelNotInDatabase {
        NumericalAperture na = new NumericalAperture(5, 6, 7, 8);
        Channel channel1 = new Channel(1e-9, 1, 2, 3, 4);
        IOpticalPropagation propagation1 = createOpticalPropagation(channel1, na, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16);

        Channel channel2 = new Channel(10e-9, 5, 6, 7, 8);
        IOpticalPropagation propagation2 = createOpticalPropagation(channel2, na, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7,
                0.8, 0.9, 0.10, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16);

        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);
        setup.setChannel(1, channel1);
        setup.setChannel(2, channel2);
        setup.setNumericalAperture(na);

        XMLOpticalPropagationDBIO dbIO = new XMLOpticalPropagationDBIO();
        IOpticalPropagationDB db = dbIO.read();
        db.add(propagation1);
        db.add(propagation2);

        String sourcePath = OpticalPropagationToYamlTest.class.getResource("").getFile();

        File root = new File(sourcePath, "OpticalPropagationToYaml");
        root.mkdir();
        OpticalPropagationToYaml writer = new OpticalPropagationToYaml();
        writer.write(root, setup, db);

    }

    private IOpticalPropagation createOpticalPropagation(IChannel channel, INumericalAperture na, double xx_0,
            double xx_45, double xx_90, double xx_135, double yy_0, double yy_45, double yy_90, double yy_135,
            double zz_0, double zz_45, double zz_90, double zz_135, double xy_0, double xy_45, double xy_90,
            double xy_135) {
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, xx_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, xx_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, xx_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, xx_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, yy_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, yy_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, yy_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, yy_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, zz_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, zz_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, zz_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, zz_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, xy_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, xy_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, xy_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, xy_135);

        return opticalPropagation;
    }

}