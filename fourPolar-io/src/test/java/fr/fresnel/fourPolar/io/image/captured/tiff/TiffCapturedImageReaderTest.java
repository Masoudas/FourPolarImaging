package fr.fresnel.fourPolar.io.image.captured.tiff;

import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.checker.TiffCapturedImageChecker;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class TiffCapturedImageReaderTest {
    private static File _testResource;

    @BeforeAll
    private static void setTestResouce() {
        _testResource = new File(TiffCapturedImageReaderTest.class.getResource("TiffCapturedImageReader").getPath());
    }

    @Test
    public void read_OneCamOneSingleChannel_ShouldShowImage()
            throws IllegalArgumentException, IOException, InterruptedException, KeyException, IncompatibleCapturedImage,
            NoReaderFoundForImage, ConverterToImgLib2NotFound {
        final File pol0_45_90_135 = new File(_testResource, "OneCam.tif");
        ImageFactory factory = new ImgLib2ImageFactory();

        final TiffCapturedImageReader imgReader = new TiffCapturedImageReader(factory);

        final ICapturedImage capturedImage = imgReader.read();

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(capturedImage.getImage(), UINT16.zero()));
        TimeUnit.SECONDS.sleep(10);

    }
}

class BuilderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;

    public void setFileSet(String label, ICapturedImageFile[] file) {
        this.files.put(label, file);
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return this.files.get(label);
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return this._cameras;
    }

    @Override
    public boolean hasLabel(String label) {
        return this.files.containsKey(label);
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.files.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();
    }

}

class BuilderDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public BuilderDummyCapturedImageFile(int[] channels, File file) {
        _channels = channels;
        _file = file;
    }

    @Override
    public int[] channels() {
        return this._channels;
    }

    @Override
    public File file() {
        return this._file;
    }

}