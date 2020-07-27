package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.io.PathFactoryOfProject;

public class GaugeFigurePathUtilTest {
    @Test
    public void createRoot_UsingSessionNameAndChannelAndFileSet_ReturnsCorrectRoot() {
        File root4PProject = new File("/");
        String visualizationSession = "TestSession";
        String setName = "TestSet";
        int channel = 1;

        GFPUTDummyFileSet fileSet = new GFPUTDummyFileSet(setName);

        File expectedRoot = new File(PathFactoryOfProject.getFolder_Visualization(root4PProject),
                visualizationSession + "/TestSet/Channel " + channel);

        File calculatedRoot = GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, channel, fileSet);

        assertEquals(expectedRoot, calculatedRoot);
    }

    @Test
    public void createRoot_UsingSessionNameAndGaugeFigure_ReturnsCorrectRoot() {
        File root4PProject = new File("/");
        String visualizationSession = "TestSession";
        String setName = "TestSet";
        int channel = 1;

        // Result is independent of gauge type and localization
        AngleGaugeType dummyGaugeType = AngleGaugeType.Rho2D;
        GaugeFigureLocalization dummyLocalization = GaugeFigureLocalization.SINGLE_DIPOLE;

        GFPUTDummyFileSet fileSet = new GFPUTDummyFileSet(setName);
        GFPUTDummyGaugeFigure gaugeFigure = new GFPUTDummyGaugeFigure(dummyGaugeType, fileSet, dummyLocalization,
                channel);

        File expectedRoot = new File(PathFactoryOfProject.getFolder_Visualization(root4PProject),
                visualizationSession + "/TestSet/Channel " + channel);

        File calculatedRoot = GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, gaugeFigure);

        assertEquals(expectedRoot, calculatedRoot);
    }

    @Test
    public void getGaugeFigureName_WholeSampleRho2D_Returns_localization_gaugeType() {
        GaugeFigureLocalization localization = GaugeFigureLocalization.WHOLE_SAMPLE;
        AngleGaugeType type = AngleGaugeType.Rho2D;

        String expectedName = localization + "_" + type;
        String figureName = GaugeFigurePathUtil.getGaugeFigureName(localization, type);

        assertEquals(expectedName, figureName);
    }

    @Test
    public void getGaugeFigureName_UsingGaugeFigure_Returns_localization_gaugeType() {
        GaugeFigureLocalization localization = GaugeFigureLocalization.WHOLE_SAMPLE;
        AngleGaugeType type = AngleGaugeType.Rho2D;
        int dummyChannel = -1;

        GFPUTDummyGaugeFigure gaugeFigure = new GFPUTDummyGaugeFigure(type, null, localization, dummyChannel);

        String expectedName = localization + "_" + type;
        String figureName = GaugeFigurePathUtil.getGaugeFigureName(gaugeFigure);

        assertEquals(expectedName, figureName);
    }
}

class GFPUTDummyFileSet implements ICapturedImageFileSet {
    String setName;

    public GFPUTDummyFileSet(String setName) {
        this.setName = setName;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return setName;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}

class GFPUTDummyGaugeFigure implements IGaugeFigure {
    AngleGaugeType _type;
    ICapturedImageFileSet _fileSet;
    GaugeFigureLocalization _localization;
    int _channel;

    public GFPUTDummyGaugeFigure(AngleGaugeType _type, ICapturedImageFileSet _fileSet,
            GaugeFigureLocalization _localization, int _channel) {
        this._type = _type;
        this._fileSet = _fileSet;
        this._localization = _localization;
        this._channel = _channel;
    }

    @Override
    public AngleGaugeType getGaugeType() {
        return _type;
    }

    @Override
    public GaugeFigureLocalization getLocalization() {
        return _localization;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

    @Override
    public int getChannel() {
        return _channel;
    }

}