package fr.fresnel.fourPolar.core.util.colorMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.util.colormap.ColorMapNotFound;

public class ColorMapFactoryTest {
    @Test
    public void create_AllColorMaps_CreatesColorMap()
            throws IllegalArgumentException, IllegalAccessException, ColorMapNotFound {
        boolean equals = true;
        Field[] allFields = ColorMapFactory.class.getDeclaredFields();

        for (Field field : allFields) {
            Object colorMap = field.get(new ColorMapFactory());
            if (colorMap instanceof String){
                equals &= ColorMapFactory.create((String)colorMap).getColorMap().equals((String)colorMap);
            }
            
        }
        
        assertTrue(equals);
    }

    
}