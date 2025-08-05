package com.jmorla.tstack.converters;

import com.jmorla.tstack.entities.TimeFrame;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Arrays;

@ReadingConverter
public class IntegerToTimeFrameConverter implements Converter<Integer, TimeFrame> {
    
    @Override
    public TimeFrame convert(Integer source) {
        return Arrays.stream(TimeFrame.values())
                .filter(timeFrame -> timeFrame.getValue() == source)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TimeFrame value: " + source));
    }
}