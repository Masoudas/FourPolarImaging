package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

/**
 * A concrete factory to generate instances of images that comply with the
 * {@link Image} using ImgLib2.
 * 
 * @param <T> is the desired pixel type.
 */
public class ImgLib2ImageFactory implements ImageFactory {
    @Override
    public <T extends PixelType> Image<T> create(long[] dim, T pixelType) {
        IMetadata defaultMetadata = new Metadata.MetadataBuilder().build();
        return this.create(dim, pixelType, defaultMetadata);
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link UnsignedShortType} image
     * @param imgLib2Type is an instance of UnsignedShortType
     * @param IMetadata   is the metadata associated with this image.
     */
    public Image<UINT16> create(Img<UnsignedShortType> img, UnsignedShortType imgLib2Type) {
        IMetadata defaulMetadata = new Metadata.MetadataBuilder().build();
        return this.create(img, imgLib2Type, defaulMetadata);
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link UnsignedShortType} image
     * @param imgLib2Type is an instance of UnsignedShortType
     * @param IMetadata   is the metadata associated with this image.
     */
    public Image<UINT16> create(Img<UnsignedShortType> img, UnsignedShortType imgLib2Type, IMetadata metadata) {
        try {
            return new ImgLib2Image<UINT16, UnsignedShortType>(img,
                    TypeConverterFactory.getConverter(UINT16.zero(), imgLib2Type), this, metadata);
        } catch (ConverterNotFound e) {
            // Exception never caught due to proper type handling
        }
        return null;
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link FloatType} image
     * @param imgLib2Type is an instance of FloatType
     */
    public Image<Float32> create(Img<FloatType> img, FloatType imgLib2Type) {
        IMetadata defaulMetadata = new Metadata.MetadataBuilder().build();

        return this.create(img, imgLib2Type, defaulMetadata);
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link FloatType} image
     * @param imgLib2Type is an instance of FloatType
     * @param IMetadata   is the metadata associated with this image.
     */
    public Image<Float32> create(Img<FloatType> img, FloatType imgLib2Type, IMetadata metadata) {
        try {
            return new ImgLib2Image<Float32, FloatType>(img,
                    TypeConverterFactory.getConverter(Float32.zero(), imgLib2Type), this, metadata);
        } catch (ConverterNotFound e) {
            // Exception never caught due to proper type handling
        }
        return null;
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link ARGBType} image
     * @param imgLib2Type is an instance of ARGBType
     */
    public Image<RGB16> create(Img<ARGBType> img, ARGBType imgLib2Type) {
        IMetadata defaulMetadata = new Metadata.MetadataBuilder().build();
        return this.create(img, imgLib2Type, defaulMetadata);
    }

    /**
     * Used for creating an {@link Image} from an existing ImgLib2 image.
     * 
     * @param img         is a {@link ARGBType} image
     * @param imgLib2Type is an instance of ARGBType
     * @param IMetadata   is the metadata associated with this image.
     */
    public Image<RGB16> create(Img<ARGBType> img, ARGBType imgLib2Type, IMetadata metadata) {
        try {
            return new ImgLib2Image<RGB16, ARGBType>(img, TypeConverterFactory.getConverter(RGB16.zero(), imgLib2Type),
                    this, metadata);
        } catch (ConverterNotFound e) {
            // Exception never caught due to proper type handling
        }
        return null;
    }

    /**
     * Creates the proper image for the given dimensionality.
     * 
     * @param <T>
     * @param dim
     * @param t
     * @return
     */
    private <U extends NativeType<U>> Img<U> _chooseImgFactory(long[] dim, U u) {
        return Util.getSuitableImgFactory(new FinalDimensions(dim), u).create(dim);
    }

    @Override
    public <T extends PixelType> Image<T> create(long[] dim, T pixelType, IMetadata metadata) {
        Image<T> _image = null;
        switch (pixelType.getType()) {
            case UINT_16:
                try {
                    UnsignedShortType type = new UnsignedShortType();
                    Img<UnsignedShortType> img = _chooseImgFactory(dim, type);
                    TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory
                            .getConverter(UINT16.zero(), type);

                    _image = new ImgLib2Image<T, UnsignedShortType>(img,
                            (TypeConverter<T, UnsignedShortType>) converter, this, metadata);
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case FLOAT_32:
                try {
                    FloatType type = new FloatType();
                    Img<FloatType> img = _chooseImgFactory(dim, type);
                    TypeConverter<Float32, FloatType> converter = TypeConverterFactory.getConverter(Float32.zero(),
                            type);

                    _image = new ImgLib2Image<T, FloatType>(img, (TypeConverter<T, FloatType>) converter, this,
                            metadata);
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case RGB_16:
                try {
                    ARGBType type = new ARGBType();
                    Img<ARGBType> img = _chooseImgFactory(dim, type);
                    TypeConverter<RGB16, ARGBType> converter = TypeConverterFactory.getConverter(RGB16.zero(), type);

                    _image = new ImgLib2Image<T, ARGBType>(img, (TypeConverter<T, ARGBType>) converter, this, metadata);
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }

            default:
                break;
        }

        return _image;
    }

}