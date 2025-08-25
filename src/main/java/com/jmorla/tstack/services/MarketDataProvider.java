package com.jmorla.tstack.services;

import com.jmorla.tstack.models.InstrumentOverview;

import java.util.List;

public interface MarketDataProvider {

    List<InstrumentOverview> getAvailableInstruments();

}
