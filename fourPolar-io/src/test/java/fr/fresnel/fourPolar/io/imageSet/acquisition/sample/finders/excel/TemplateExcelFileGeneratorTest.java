package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class TemplateExcelFileGeneratorTest {
    File rootFolder;

    @BeforeAll
    public void getRoot() {
        this.rootFolder = new File(TemplateExcelFileGeneratorTest.class.getResource("").getPath(),
                "/TemplateExcelFileGeneratorTestMaterial");
        this.rootFolder.mkdir();
    }

    @Test
    public void create_OneCameraChannel1_CreatesThefileInTheResourceTestMaterialFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.One, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(1));
    }

    @Test
    public void create_TwoCameraChannel2_CreatesThefileInTheResourceTestMaterialFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.Two, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(2));
    }

    @Test
    public void create_FourCameraChannel3_CreatesThefileInTheResourceTestMaterialFolder() throws IOException {
        TemplateExcelFileGenerator fileGenerator = new TemplateExcelFileGenerator(Cameras.Four, this.rootFolder);

        // If no exceptions are caught, the file has been created.
        assertTrue(fileGenerator.createChannelFile(3));
    }
}