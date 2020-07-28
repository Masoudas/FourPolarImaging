package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.vector.svg;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;

public class SVGVectorGaugeFigureReaderTest {
    private File _root = _createRoot();

    private VectorImageFactory _defaultFactory = new BatikVectorImageFactory();

    public File _createRoot() {
        File root = new File(SVGVectorGaugeFigureWriterTest.class.getResource("").getPath(), "ReaderTests");
        if (!root.exists()) {
            root.mkdirs();
        }

        return root;
    }

    @Test
    public void read_NonExistentGaugeFigure_ThrowsExceptionWithCompleteInfo() {
        String visualizationSession = "NonExistentSession";
        String setName = "NonExistentSet";
        ReaderDummyFileSet fileSet = new ReaderDummyFileSet(setName);
        int channel = 1;

        AngleGaugeType gaugeType = AngleGaugeType.Rho2D;
        GaugeFigureLocalization localization = GaugeFigureLocalization.SINGLE_DIPOLE;

        SVGVectorGaugeFigureReader reader = new SVGVectorGaugeFigureReader(_defaultFactory, localization, gaugeType);

        GaugeFigureIOException exeption = assertThrows(GaugeFigureIOException.class, () -> {
            reader.read(_root, visualizationSession, channel, fileSet);
        });

        assertTrue(exeption.angleGaugeType() == gaugeType);
        assertTrue(exeption.channel() == channel);
        assertTrue(exeption.fileSet() == fileSet);
        assertTrue(exeption.localization() == localization);
        assertTrue(exeption.visualizationSession().equals(visualizationSession));
    }

    @Test
    public void read_GaugeFigureWithOneCoordinate_ReturnsTheSingleImage() throws GaugeFigureIOException {
        File root4PProject = new File(_root, "MultipleImage");
        String visualizationSession = "TestSession";
        String setName = "TestSet1";
        ReaderDummyFileSet fileSet = new ReaderDummyFileSet(setName);
        int channel = 1;

        AngleGaugeType gaugeType = AngleGaugeType.Rho2D;
        GaugeFigureLocalization localization = GaugeFigureLocalization.SINGLE_DIPOLE;

        SVGVectorGaugeFigureReader reader = new SVGVectorGaugeFigureReader(_defaultFactory, localization, gaugeType);

        VectorGaugeFigure figure = (VectorGaugeFigure) reader.read(root4PProject, visualizationSession, channel,
                fileSet);

        assertTrue(figure.getChannel() == channel);
        assertTrue(figure.getGaugeType() == gaugeType);
        assertTrue(figure.getLocalization() == localization);

        IMetadata metadata = figure.getVectorImage().metadata();
        long[] expected_dim = { 1, 1, 1, 1, 1 };
        assertArrayEquals(expected_dim, metadata.getDim());
    }

    @Test
    public void read_TwoGaugeFigures_ReturnsEachImage() throws GaugeFigureIOException {
        File root4PProject = new File(_root, "MultipleImage");
        String visualizationSession = "TestSession";

        // Set 1
        String set1Name = "TestSet1";
        ReaderDummyFileSet fileSet1 = new ReaderDummyFileSet(set1Name);
        AngleGaugeType gaugeType1 = AngleGaugeType.Rho2D;
        GaugeFigureLocalization localization1 = GaugeFigureLocalization.SINGLE_DIPOLE;
        int channel1 = 1;

        // Set 2
        String set2Name = "TestSet2";
        ReaderDummyFileSet fileSet2 = new ReaderDummyFileSet(set2Name);
        AngleGaugeType gaugeType2 = AngleGaugeType.Delta2D;
        GaugeFigureLocalization localization2 = GaugeFigureLocalization.WHOLE_SAMPLE;
        int channel2 = 1;
    
        SVGVectorGaugeFigureReader reader = new SVGVectorGaugeFigureReader(_defaultFactory, localization1, gaugeType1);
        VectorGaugeFigure figure1 = (VectorGaugeFigure) reader.read(root4PProject, visualizationSession, channel1,
                fileSet1);
        long[] expected_dim1 = { 1, 1, 1, 1, 1 };
        _checkGaugeFigureMatchExpectedFigure(figure1, gaugeType1, localization1, channel1, expected_dim1);

        reader.setAngleGaugeType(gaugeType2);
        reader.setLocalization(localization2);
        VectorGaugeFigure figure2 = (VectorGaugeFigure) reader.read(root4PProject, visualizationSession, channel2,
                fileSet2);
        long[] expected_dim2 = { 1, 1, 1, 1, 1 };
        _checkGaugeFigureMatchExpectedFigure(figure2, gaugeType2, localization2, channel2, expected_dim2);

    }

    private void _checkGaugeFigureMatchExpectedFigure(VectorGaugeFigure figure, AngleGaugeType gaugeType,
            GaugeFigureLocalization localization, int channel, long[] expected_dim) {
        assertTrue(figure.getChannel() == channel);
        assertTrue(figure.getGaugeType() == gaugeType);
        assertTrue(figure.getLocalization() == localization);
        assertArrayEquals(expected_dim, figure.getVectorImage().metadata().getDim());
    }

}

class ReaderDummyFileSet implements ICapturedImageFileSet {
    String setName;

    public ReaderDummyFileSet(String setName) {
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