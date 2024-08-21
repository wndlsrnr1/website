package com.website.exception.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.website.exception.ErrorCode;

import java.io.IOException;

public class ErrorCodeSerializer extends JsonSerializer<ErrorCode> {
    @Override
    public void serialize(ErrorCode errorCode, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", errorCode.name());
        gen.writeNumberField("httpStatus", errorCode.getHttpStatus().value());
        gen.writeStringField("clientMessage", errorCode.getClientMessage());
        gen.writeEndObject();
    }
}
