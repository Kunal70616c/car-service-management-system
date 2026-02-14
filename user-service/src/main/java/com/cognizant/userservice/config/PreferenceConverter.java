package com.cognizant.userservice.config;

import com.cognizant.userservice.model.Preference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class PreferenceConverter implements AttributeConverter<Preference, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Preference preference) {
        if (preference == null) return null;
        try {
            return objectMapper.writeValueAsString(preference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Preference to JSON", e);
        }
    }

    @Override
    public Preference convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return objectMapper.readValue(dbData, Preference.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Preference", e);
        }
    }
}