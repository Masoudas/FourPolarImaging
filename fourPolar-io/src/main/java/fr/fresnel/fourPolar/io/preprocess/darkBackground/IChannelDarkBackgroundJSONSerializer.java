package fr.fresnel.fourPolar.io.preprocess.darkBackground;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class IChannelDarkBackgroundJSONSerializer extends StdSerializer<IChannelDarkBackgroundToJSONAdaptor> {
    /**
     *
     */
    private static final long serialVersionUID = -4131618816994995889L;

    public IChannelDarkBackgroundJSONSerializer() {
        this(null);
    }

    protected IChannelDarkBackgroundJSONSerializer(Class<IChannelDarkBackgroundToJSONAdaptor> t) {
        super(t);
    }

    @Override
    public void serialize(IChannelDarkBackgroundToJSONAdaptor value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        for (Polarization polarization : Polarization.values()) {
            gen.writeObjectField(polarization.name(), value.getDarkBackgrounds(polarization));
        }
        gen.writeEndObject();

    }

}