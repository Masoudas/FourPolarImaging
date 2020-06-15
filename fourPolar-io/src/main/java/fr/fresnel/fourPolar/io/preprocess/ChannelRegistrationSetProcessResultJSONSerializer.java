package fr.fresnel.fourPolar.io.preprocess;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

class ChannelRegistrationSetProcessResultJSONSerializer
        extends StdSerializer<ChannelRegistrationSetProcessResultToJSONAdaptor> {
    private static final long serialVersionUID = 2929222548830461961L;

    public ChannelRegistrationSetProcessResultJSONSerializer() {
        this(null);
    }

    public ChannelRegistrationSetProcessResultJSONSerializer(
            Class<ChannelRegistrationSetProcessResultToJSONAdaptor> t) {
        super(t);
    }

    @Override
    public void serialize(ChannelRegistrationSetProcessResultToJSONAdaptor value, JsonGenerator gen,
            SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("Dark Background Level", value.getDarkBackgroundJSONAdaptor());
        gen.writeObjectField("Registration Result", value.getRegistrationResultJSONAdaptor());

        gen.writeEndObject();

    }

}