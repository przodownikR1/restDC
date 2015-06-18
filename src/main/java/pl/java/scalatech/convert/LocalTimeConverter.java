package pl.java.scalatech.convert;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeConverter implements org.springframework.core.convert.converter.Converter<String, LocalTime>, AttributeConverter<LocalTime, Time> {
    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ISO_TIME);
    }

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
        return Time.valueOf(localTime);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time time) {
        return time.toLocalTime();
    }
}