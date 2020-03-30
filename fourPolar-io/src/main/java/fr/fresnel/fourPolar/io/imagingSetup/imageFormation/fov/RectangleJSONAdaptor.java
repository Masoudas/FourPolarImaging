package fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.util.DRectangle;

/**
 * This class is used as an adaptor of {@link DRectangle} to JSON.
 */
public class RectangleJSONAdaptor {
    @JsonProperty("[xTop, yTop]")
    private String _top;

    @JsonProperty("[width, height]")
    private String _size;

    /**
     * Allows the class to be written to JSON as an instance of this class.
     * @param rectangle
     */
    public void toYaml(DRectangle rectangle) {
        _setTop(rectangle);
        _setSize(rectangle);
    }

    /**
     * Returns the class from the JSON file.
     * @return
     */
    public DRectangle fromYaml() {
        int[] top = _stringToInt(this._top);
        int[] size = _stringToInt(this._size);

        return new DRectangle(top[0], top[1], size[0], size[1]);
    }

    private void _setTop(DRectangle rectangle) {
        int[] topArr = {rectangle.xtop, rectangle.ytop};
        _top =  Arrays.toString(topArr);
    }

    private void _setSize(DRectangle rectangle) {
        int[] topArr = {rectangle.width, rectangle.height};
        _size =  Arrays.toString(topArr);
    }

    private int[] _stringToInt(String arr){
        String[] subStr = arr.substring(1, arr.length() - 1).split(", ");
        
        int[] newArr = new int[2];
        for (int ctr = 0; ctr < 2; ctr++) {
            newArr[ctr] = Integer.parseInt(subStr[ctr]);
            System.out.println(newArr[ctr]);
        }
        
        return newArr;
    }

    
}