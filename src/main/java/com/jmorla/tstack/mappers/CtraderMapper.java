package com.jmorla.tstack.mappers;

import com.jmorla.tstack.models.SymbolRecord;
import com.xtrader.protocol.openapi.v2.model.ProtoOALightSymbol;

public final class CtraderMapper {

    public static SymbolRecord mapToSymbolRecord(ProtoOALightSymbol proto) {
        return new SymbolRecord(
                proto.getSymbolId(),
                proto.getSymbolName(),
                proto.getDescription(),
                proto.getEnabled()
        );
    }
}
