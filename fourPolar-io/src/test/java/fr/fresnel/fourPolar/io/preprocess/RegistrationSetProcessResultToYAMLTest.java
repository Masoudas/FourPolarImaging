package fr.fresnel.fourPolar.io.preprocess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

public class RegistrationSetProcessResultToYAMLTest {
    @Test
    public void write_2ChannelSetupResults_WritesYamlInTheDestinationWhereParentFolderIsTargetFolder()
            throws IOException {
        File root = new File(RegistrationSetProcessResultToYAMLTest.class.getResource("").getPath());

        DummyFPSetup setup = new DummyFPSetup(2);

        String backgroundEstimMethod = "Dummy Method";
        DummyChannelBackground background_c1 = new DummyChannelBackground(1, 1, 1, 1, 1, backgroundEstimMethod);
        DummyChannelBackground background_c2 = new DummyChannelBackground(2, 1, 1, 1, 1, backgroundEstimMethod);

        String registrationMethod = "Dummy Method";
        DummyChannelRegistrationResult registrationResult_c1 = new DummyChannelRegistrationResult(1, 1.2,
                registrationMethod, new Affine2D(), new Affine2D(), new Affine2D());
        DummyChannelRegistrationResult registrationResult_c2 = new DummyChannelRegistrationResult(2, 1.2,
                registrationMethod, new Affine2D(), new Affine2D(), new Affine2D());

        RegistrationSetProcessResult setResult = new RegistrationSetProcessResult(2);
        setResult.setDarkBackground(1, background_c1);
        setResult.setDarkBackground(2, background_c2);
        setResult.setRegistrationResult(1, registrationResult_c1);
        setResult.setRegistrationResult(2, registrationResult_c2);

        new RegistrationSetProcessResultToYAML(setup, setResult).write(root);

    }

}

class DummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private int numChannels;

    public DummyFPSetup(int numChannels) {
        this.numChannels = numChannels;
    }

    @Override
    public Cameras getCameras() {
        return cameras;
    }

    @Override
    public void setCameras(Cameras cameras) throws IllegalArgumentException {
        this.cameras = cameras;
    }

    @Override
    public IChannel getChannel(int channel) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException {
    }

    @Override
    public int getNumChannel() {
        return this.numChannels;
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return null;
    }

    @Override
    public void setNumericalAperture(INumericalAperture na) {
    }

    @Override
    public IFieldOfView getFieldOfView() {
        return null;
    }

    @Override
    public void setFieldOfView(IFieldOfView fov) throws IllegalArgumentException {
    }

}

class DummyChannelBackground implements IChannelDarkBackground {
    private final double _pol0;
    private final double _pol45;
    private final double _pol90;
    private final double _pol135;

    private final int _channel;
    private final String _method;

    public DummyChannelBackground(int channel, double pol0, double pol45, double pol90, double pol135, String method) {
        this._channel = channel;

        _pol0 = pol0;
        _pol45 = pol45;
        _pol90 = pol90;
        _pol135 = pol135;

        this._method = method;
    }

    @Override
    public double getBackgroundLevel(Polarization polarization) {
        switch (polarization) {
            case pol0:
                return _pol0;

            case pol45:
                return _pol45;

            case pol90:
                return _pol90;

            case pol135:
                return _pol135;

            default:
                return -1;
        }
    }

    @Override
    public int channel() {
        return this._channel;
    }

    @Override
    public String estimationMethod() {
        return this._method;
    }

}

class DummyChannelRegistrationResult implements IChannelRegistrationResult {
    int channel;
    HashMap<RegistrationRule, Affine2D> affines = new HashMap<>();
    String method;
    double error;

    public DummyChannelRegistrationResult(int channel, double error, String method, Affine2D... affine) {
        this.channel = channel;
        for (int i = 0; i < affine.length; i++) {
            affines.put(RegistrationRule.values()[i], affine[i]);
        }
        this.method = method;
        this.error = error;
    }

    @Override
    public Affine2D getAffineTransform(RegistrationRule rule) {
        return affines.get(rule);
    }

    @Override
    public double error(RegistrationRule rule) {
        return this.error;
    }

    @Override
    public int channel() {
        return this.channel;
    }

    @Override
    public String registrationMethod() {
        return this.method;
    }

}