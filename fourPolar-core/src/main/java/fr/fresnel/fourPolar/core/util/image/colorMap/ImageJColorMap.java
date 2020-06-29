package fr.fresnel.fourPolar.core.util.image.colorMap;

import java.io.IOException;
import java.net.URL;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
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

    private final ARGB8 _color;

    public static ImageJColorMap getDefaultColorMaps(String colorMap) {
        ImageJColorMap jColorMap = null;
        LUTService lService = new DefaultLUTService();
        try {
            jColorMap = new ImageJColorMap(colorMap, lService.loadLUT(lService.findLUTs().get(colorMap)));
        } catch (IOException e) {
            // Through proper choice of colormaps, this exception is never caught.
        }

        return jColorMap;
    }

    public static ImageJColorMap getDiskColorMap(String colorMap) {
        URL path = ColorMapFactory.class.getResource(colorMap);
        ImageJColorMap jColorMap = null;
        LUTService lService = new DefaultLUTService();
        try {
            jColorMap = new ImageJColorMap(colorMap, lService.loadLUT(path));
        } catch (IOException e) {
            // Through proper choice of colormaps, this exception is never caught.
        }

        return jColorMap;

    }

    private ImageJColorMap(String colorMap, ColorTable colorTable) {
        this._colorMap = colorMap;
        this._cTable = colorTable;
        this._color = new ARGB8(0, 0, 0);

    }

    @Override
    public ARGB8 getColor(double min, double max, double val) {
        int index = this._cTable.lookupARGB(min, max, val);
        this._color.set(ARGBType.red(index), ARGBType.green(index), ARGBType.blue(index), ARGBType.alpha(index));
        return _color;
    }

    @Override
    public String getColorMap() {
        return _colorMap;
    }

}