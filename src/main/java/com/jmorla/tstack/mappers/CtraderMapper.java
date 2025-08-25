package com.jmorla.tstack.mappers;

import com.jmorla.tstack.models.InstrumentOverview;
import com.xtrader.protocol.openapi.v2.model.ProtoOALightSymbol;

public final class CtraderMapper {

    public static InstrumentOverview mapToSymbolRecord(ProtoOALightSymbol proto) {
        return new InstrumentOverview(
                String.valueOf(proto.getSymbolId()),
                proto.getSymbolName(),
                proto.getDescription()
        );
    }
}
