package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.vector.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.io.PathFactoryOfProject;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;

public class SVGVectorGaugeFigureWriterTest {
    private VectorImageFactory _batikFactory = new BatikVectorImageFactory();
    private File _root = _createRoot();

    private long[] _defaultDim = new long[] { 1, 1, 1, 1, 1 };

    public File _createRoot() {
        File root = new File(SVGVectorGaugeFigureWriterTest.class.getResource("").getPath(), "WriterTests");
        if (!root.exists()) {
            root.mkdirs();
        }

        return root;
    }

    @Test
    public void _write_GaugeImageWholeSampleRho2DStick_CreatesOneImageInTheImageNameFolder()
            throws GaugeFigureIOException {
        String visualizationSession = "TestSession";
        String setName = "TestSet";

        AngleGaugeType gaugeType = AngleGaugeType.Rho2D;
        GaugeFigureLocalization localization = GaugeFigureLocalization.SINGLE_DIPOLE;

        VectorGaugeFigure gaugeFigure = _createDefaultBatikGaugeImage(localization, gaugeType, setName);

        SVGVectorGaugeFigureWriter writer = new SVGVectorGaugeFigureWriter();
        writer.write(_root, visualizationSession, gaugeFigure);

        assertTrue(_isDefaultGaugeImageOnDisk(visualizationSession, setName, gaugeType, localization));

    }

    @Test
    public void _write_TwoGaugeFigures_WritesBothFiguresInPath() throws GaugeFigureIOException {
        String visualizationSession = "TestSession";

        String setName1 = "TestSet1";
        AngleGaugeType gaugeType1 = AngleGaugeType.Rho2D;
        GaugeFigureLocalization localization1 = GaugeFigureLocalization.SINGLE_DIPOLE;
        VectorGaugeFigure gaugeFigure1 = _createDefaultBatikGaugeImage(localization1, gaugeType1, setName1);

        String setName2 = "TestSet2";
        AngleGaugeType gaugeType2 = AngleGaugeType.Delta2D;
        GaugeFigureLocalization localization2 = GaugeFigureLocalization.WHOLE_SAMPLE;
        VectorGaugeFigure gaugeFigure2 = _createDefaultBatikGaugeImage(localization2, gaugeType2, setName2);

        SVGVectorGaugeFigureWriter writer = new SVGVectorGaugeFigureWriter();

        writer.write(_root, visualizationSession, gaugeFigure1);
        writer.write(_root, visualizationSession, gaugeFigure2);

        assertTrue(_isDefaultGaugeImageOnDisk(visualizationSession, setName1, gaugeType1, localization1));
        assertTrue(_isDefaultGaugeImageOnDisk(visualizationSession, setName2, gaugeType2, localization2));

    }

    private VectorGaugeFigure _createDefaultBatikGaugeImage(GaugeFigureLocalization localization,
            AngleGaugeType gaugeType, String setName) {
        IMetadata metadata = new Metadata.MetadataBuilder(_defaultDim).axisOrder(IGaugeFigure.AXIS_ORDER).build();
        VectorImage vectorImage = _batikFactory.create(metadata);
        WriterDummyFileSet fileSet = new WriterDummyFileSet(setName);
        long channel = _defaultDim[IGaugeFigure.AXIS_ORDER.c_axis];

        return VectorGaugeFigure.create(localization, gaugeType, vectorImage, fileSet, (int) channel);

    }

    /**
     * Checks whether default image folder is on disk, and contains only two files
     * (metadata and svg image).
     */
    private boolean _isDefaultGaugeImageOnDisk(String visualizationSession, String setName, AngleGaugeType gaugeType,
            GaugeFigureLocalization localization) {
        File root = new File(PathFactoryOfProject.getFolder_Visualization(_root),
                visualizationSession + "/" + setName + "/" + "Channel " + _defaultDim[IGaugeFigure.AXIS_ORDER.c_axis]
                        + "/" + localization + "_" + gaugeType);

        return root.exists() && root.listFiles().length == 2;
    }

}

class WriterDummyFileSet implements ICapturedImageFileSet {
    String setName;

    public WriterDummyFileSet(String setName) {
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