package fr.fresnel.fourPolar.io.preprocess.registration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

public class IChannelRegistrationResultJSONSerializer extends StdSerializer<IChannelRegistrationResultToJSONAdaptor> {
    private static final long serialVersionUID = 1453078587635922958L;

    public IChannelRegistrationResultJSONSerializer() {
        this(null);
    }

    public IChannelRegistrationResultJSONSerializer(Class<IChannelRegistrationResultToJSONAdaptor> t) {
        super(t);
    }

    @Override
    public void serialize(IChannelRegistrationResultToJSONAdaptor value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        for (RegistrationRule rule : RegistrationRule.values()) {
            gen.writeObjectField(rule.description, value.getRuleAdaptor(rule));
        }
        gen.writeEndObject();

    }

}