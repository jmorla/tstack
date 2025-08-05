package com.jmorla.tstack.converters;

import com.jmorla.tstack.entities.TimeFrame;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class TimeFrameToIntegerConverter implements Converter<TimeFrame, Integer> {
    
    @Override
    public Integer convert(TimeFrame source) {
        return source.getValue();
    }
}