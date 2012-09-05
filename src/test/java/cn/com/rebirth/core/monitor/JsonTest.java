package cn.com.rebirth.core.monitor;

import java.io.IOException;

import cn.com.rebirth.commons.io.FastByteArrayOutputStream;
import cn.com.rebirth.core.mapper.JsonMapper;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		JsonFactory jsonFactory = jsonMapper.getMapper().getJsonFactory();
		JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(new FastByteArrayOutputStream(),
				JsonEncoding.UTF8);
		jsonGenerator.writeFieldName("jvm");
		jsonGenerator.writeStartObject();
		jsonGenerator.writeEndObject();
	}

}
