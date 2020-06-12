package fr.fresnel.fourPolar.io.util.shape;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * This class is used as an adaptor of {@link IBoxShape} to JSON.
 */
public class IBoxShape2DJSONAdaptor {
    @JsonProperty("[xTop, yTop]")
    private String _top;

    @JsonProperty("[xBottom, yBottom]")
    private String _bottom;

    /**
     * Allows the class to be written to JSON as an instance of this class.
     * 
     * @param rectangle
     * 
     * @throws IllegalArgumentException if the shape is not 2D.
     */
    public void toYaml(IBoxShape rectangle) {
        if (rectangle.axisOrder() != AxisOrder.XY) {
            throw new IllegalArgumentException("Can't write any shape other than XY rectangle");
        }
        _bottom = Arrays.toString(rectangle.min());
        _top = Arrays.toString(rectangle.max());
    }

    /**
     * Returns the class from the JSON file.
     * 
     * @return
     */
    public IBoxShape fromYaml() {
        long[] top = _stringToLong(this._top);
        long[] bottom = _stringToLong(this._bottom);
        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

    private long[] _stringToLong(String arr) {
        String[] subStr = arr.substring(1, arr.length() - 1).split(", ");

        long[] newArr = new long[2];
        for (int ctr = 0; ctr < 2; ctr++) {
            newArr[ctr] = Long.parseLong(subStr[ctr]);
        }

        return newArr;
    }

}