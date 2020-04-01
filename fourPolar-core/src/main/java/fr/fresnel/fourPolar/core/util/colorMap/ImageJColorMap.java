package fr.fresnel.fourPolar.core.util.colorMap;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import net.imagej.lut.DefaultLUTService;
import net.imagej.lut.LUTService;
import net.imglib2.display.ColorTable;
import net.imglib2.type.numeric.ARGBType;

/**
 * This class uses the {@link DefaultLUTService} of ImageJ to create colormaps.
 * The name of the available colormaps is in the {@link ColorMapFactory}, all of
 * which start with ImageJ.
 */
class ImageJColorMap implements ColorMap {
    private ColorTable _cTable;
    private final String _colorMap;

    public ImageJColorMap(String colorMap) {
        this._colorMap = colorMap;
        LUTService lService = new DefaultLUTService();

        try {
            _cTable = lService.loadLUT(lService.findLUTs().get(colorMap));
        } catch (IOException e) {
            // Through proper choice of colormaps, this exception is never caught.
        }
    }

    @Override
    public RGB16 getColor(double min, double max, double val) {
        int index = this._cTable.lookupARGB(min, max, val);
        return new RGB16(ARGBType.red(index), ARGBType.green(index), ARGBType.blue(index));
    }

    @Override
    public String getColorMap() {
        return _colorMap;
    }
    

    
}