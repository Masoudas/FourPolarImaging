package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class TemplateExcelFileGeneratorTest {
    File rootFolder;

    @Before
    public void getRoot() {
        this.rootFolder = new File(TemplateExcelFileGeneratorTest.class.getResource("").getPath());
    }

    @Test
    public void create_OneCameraChannel1_CreatesThefileInTheResourceFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.One, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(1));
    }


    @Test
    public void create_TwoCameraChannel2_CreatesThefileInTheResourceFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.Two, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(2));
    }

    @Test
    public void create_FourCameraChannel3_CreatesThefileInTheResourceFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.Four, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(3));
    }
}