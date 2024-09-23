package smartsuite.security.jackson;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@SuppressWarnings ({"serial"})
public class BigDecimalSerializer extends NumberSerializer {
	final public static BigDecimal MAX_SAFE_INTEGER = new BigDecimal("9007199254740991"); 

	public BigDecimalSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

	@Override
	public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		if(value instanceof BigDecimal && MAX_SAFE_INTEGER.compareTo((BigDecimal)value) == -1){
			jgen.writeString(value.toString());
		}
		else{
			super.serialize(value, jgen, provider);
		}
	}
}
