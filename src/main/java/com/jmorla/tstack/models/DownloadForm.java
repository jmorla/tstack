package com.jmorla.tstack.models;

import com.jmorla.tstack.entities.TimeFrame;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DownloadForm {

    @NotNull(message = "Please select at least one symbol")
    @Size(min = 1, max = 5, message = "You can select between 1 and 5 symbols")
    private List<String> symbols;
    private TimeFrame timeframe;
    
    @Override
    public String toString() {
        return "DownloadForm{" +
                "symbols=" + symbols +
                ", timeframe=" + timeframe +
                '}';
    }
}