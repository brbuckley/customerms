package service.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.File;
import java.io.IOException;

/** Validator and serializer for JSON using Jackson. */
public class JacksonValidator {

  ObjectMapper mapper;

  /** Custom constructor. */
  public JacksonValidator() {
    mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
  }

  /**
   * Validates the given JSON file against the given schema.
   *
   * @param json The JSON string.
   * @param schema The JSON schema.
   * @return Boolean.
   * @throws IOException File related exceptions.
   */
  public boolean validate(String json, File schema) throws IOException {
    JsonNode jsonNode = mapper.readTree(json);
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
    JsonSchema jsonSchema = factory.getSchema(schema.toURI());
    return jsonSchema.validate(jsonNode).isEmpty();
  }

  /**
   * Deserializes a JSON file into a POJO.
   *
   * @param jsonFile The JSON file.
   * @param c The Class of the object.
   * @return The resulting java object.
   * @throws IOException File related exceptions.
   */
  public Object jsonToObject(File jsonFile, Class c) throws IOException {
    return mapper.readerFor(c).readValue(jsonFile);
  }

  /**
   * Serializes a POJO into a JSON file.
   *
   * @param obj The object.
   * @throws IOException File related exceptions.
   */
  public void objectToJson(Object obj) throws IOException {
    mapper.writeValue(new File("objectJson.json"), obj);
  }
}
