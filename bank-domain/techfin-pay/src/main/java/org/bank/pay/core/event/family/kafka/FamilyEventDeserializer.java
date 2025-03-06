package org.bank.pay.core.event.family.kafka;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

public class FamilyEventDeserializer extends JsonDeserializer<FamilyEvent> {

    @SneakyThrows
    @Override
    public FamilyEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Class<?> classType = Class.forName(node.get("classType").asText());
        return (FamilyEvent) jsonParser.getCodec().treeToValue(node, classType);
    }
}
