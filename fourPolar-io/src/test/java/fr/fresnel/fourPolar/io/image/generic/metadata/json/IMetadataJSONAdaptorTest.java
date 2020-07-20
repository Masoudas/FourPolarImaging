package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class IMetadataJSONAdaptorTest {
    @Test
    public void fromJSON_NullParamatersINJSONProperties_ThrowsIOException() {
        IMetadataJSONAdaptor adaptor = new IMetadataJSONAdaptor();

        assertThrows(IOException.class, ()->{adaptor.fromJSON();});
        
    }
}