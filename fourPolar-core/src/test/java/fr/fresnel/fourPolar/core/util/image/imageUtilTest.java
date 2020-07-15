package fr.fresnel.fourPolar.core.util.image;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class imageUtilTest {
    @Test
    public void getPlaneCursor_22ImageFirstPlane_ReturnsCorrectCursor() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] plane_start = { 0, 0 };
        long[] plane_len = { 2, 2 };

        _setPlaneToValue(image, plane_start, plane_len);

        IPixelCursor<UINT16> cursor = ImageUtil.getPlaneCursor(image, 1);
        _cursorsHaveSameData(image.getCursor(plane_start, plane_len), cursor);
    
    }

    @Test
    public void getPlaneCursor_223ImageSecondPlane_ReturnsCorrectCursor() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] plane_start = { 0, 0, 1 };
        long[] plane_len = { 2, 2, 1 };

        _setPlaneToValue(image, plane_start, plane_len);

        IPixelCursor<UINT16> cursor = ImageUtil.getPlaneCursor(image, 2);
        _cursorsHaveSameData(image.getCursor(plane_start, plane_len), cursor);
    
    }

    @Test
    public void getPlaneCursor_2213ImageSecondPlane_ReturnsCorrectCursor() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 3 }).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] plane_start = { 0, 0, 0, 1 };
        long[] plane_len = { 2, 2, 1, 1 };

        _setPlaneToValue(image, plane_start, plane_len);

        IPixelCursor<UINT16> cursor = ImageUtil.getPlaneCursor(image, 2);
        _cursorsHaveSameData(image.getCursor(plane_start, plane_len), cursor);
    
    }

    private void _setPlaneToValue(Image<UINT16> image, long[] plane_start, long[] plane_len){
        IPixelCursor<UINT16> pixelCursor = image.getCursor(plane_start, plane_len);
        while (pixelCursor.hasNext()) {
            IPixel<UINT16> pixel = pixelCursor.next();
            pixel.value().set(1);
            pixelCursor.setPixel(pixel);
        }
    }

    private boolean _cursorsHaveSameData(IPixelCursor<UINT16> cursor1, IPixelCursor<UINT16> cursor2) {
        if (!cursor1.hasNext() || !cursor2.hasNext()){
            return false;
        }

        boolean pixelEqual = true;
        while (cursor1.hasNext() && pixelEqual) {
            pixelEqual = cursor1.next().value().get() == cursor2.next().value().get();
        }    

        return pixelEqual;
    }

}