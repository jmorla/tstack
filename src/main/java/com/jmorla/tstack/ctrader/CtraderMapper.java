package com.jmorla.tstack.ctrader;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.jmorla.tstack.entities.TimeFrame;
import com.xtrader.protocol.openapi.v2.*;
import com.xtrader.protocol.openapi.v2.model.ProtoOAPayloadType;
import com.xtrader.protocol.openapi.v2.model.ProtoOATrendbarPeriod;
import com.xtrader.protocol.proto.commons.ProtoMessage;

import java.util.HashMap;
import java.util.Map;

public final class CtraderMapper {

    private static final Map<Class<? extends GeneratedMessage>, ProtoOAPayloadType>
            MESSAGE_TO_PAYLOAD_TYPE = new HashMap<>();

    private static final Map<Integer, Class<? extends GeneratedMessage>> PAYLOAD_TYPE_TO_MESSAGE =
            new HashMap<>();

    private static final Map<Integer, ProtoMapper<ByteString, ? extends GeneratedMessage>>
            PAYLOAD_TYPE_TO_MAPPER = new HashMap<>();


    static {
        addMapping(
                ProtoOAApplicationAuthReq.class,
                ProtoOAPayloadType.PROTO_OA_APPLICATION_AUTH_REQ,
                ProtoOAApplicationAuthReq::parseFrom);
        addMapping(
                ProtoOAApplicationAuthRes.class,
                ProtoOAPayloadType.PROTO_OA_APPLICATION_AUTH_RES,
                ProtoOAApplicationAuthRes::parseFrom);
        addMapping(
                ProtoOAAccountAuthReq.class,
                ProtoOAPayloadType.PROTO_OA_ACCOUNT_AUTH_REQ,
                ProtoOAAccountAuthReq::parseFrom);
        addMapping(
                ProtoOAAccountAuthRes.class,
                ProtoOAPayloadType.PROTO_OA_ACCOUNT_AUTH_RES,
                ProtoOAAccountAuthRes::parseFrom);
        addMapping(
                ProtoOASymbolsListReq.class,
                ProtoOAPayloadType.PROTO_OA_SYMBOLS_LIST_REQ,
                ProtoOASymbolsListReq::parseFrom);
        addMapping(
                ProtoOASymbolsListRes.class,
                ProtoOAPayloadType.PROTO_OA_SYMBOLS_LIST_RES,
                ProtoOASymbolsListRes::parseFrom);
        addMapping(
                ProtoOASymbolByIdReq.class,
                ProtoOAPayloadType.PROTO_OA_SYMBOL_BY_ID_REQ,
                ProtoOASymbolByIdReq::parseFrom);
        addMapping(
                ProtoOASymbolByIdRes.class,
                ProtoOAPayloadType.PROTO_OA_SYMBOL_BY_ID_RES,
                ProtoOASymbolByIdRes::parseFrom);
        addMapping(
                ProtoOAGetTrendbarsReq.class,
                ProtoOAPayloadType.PROTO_OA_GET_TRENDBARS_REQ,
                ProtoOAGetTrendbarsReq::parseFrom);
        addMapping(
                ProtoOAGetTrendbarsRes.class,
                ProtoOAPayloadType.PROTO_OA_GET_TRENDBARS_RES,
                ProtoOAGetTrendbarsRes::parseFrom);
        addMapping(
                ProtoOAGetTickDataReq.class,
                ProtoOAPayloadType.PROTO_OA_GET_TICKDATA_REQ,
                ProtoOAGetTickDataReq::parseFrom);
        addMapping(
                ProtoOAGetTickDataRes.class,
                ProtoOAPayloadType.PROTO_OA_GET_TICKDATA_RES,
                ProtoOAGetTickDataRes::parseFrom);
    }

    public static ProtoOATrendbarPeriod toProtoOATrendbarPeriod(TimeFrame tf) {
        return switch (tf) {
            case M1 -> ProtoOATrendbarPeriod.M1;
            case M2 -> ProtoOATrendbarPeriod.M2;
            case M3 -> ProtoOATrendbarPeriod.M3;
            case M4 -> ProtoOATrendbarPeriod.M4;
            case M5 -> ProtoOATrendbarPeriod.M5;
            case M10 -> ProtoOATrendbarPeriod.M10;
            case M15 -> ProtoOATrendbarPeriod.M15;
            case M30 -> ProtoOATrendbarPeriod.M30;
            case H1 -> ProtoOATrendbarPeriod.H1;
            case H4 -> ProtoOATrendbarPeriod.H4;
            case H12 -> ProtoOATrendbarPeriod.H12;
            case D1 -> ProtoOATrendbarPeriod.D1;
            case W1 -> ProtoOATrendbarPeriod.W1;
            case MN1 -> ProtoOATrendbarPeriod.MN1;
        };
    }

    private static void addMapping(
            Class<? extends GeneratedMessage> messageClass,
            ProtoOAPayloadType payloadType,
            ProtoMapper<ByteString, ? extends GeneratedMessage> mapper) {
        MESSAGE_TO_PAYLOAD_TYPE.put(messageClass, payloadType);
        PAYLOAD_TYPE_TO_MESSAGE.put(payloadType.getNumber(), messageClass);
        PAYLOAD_TYPE_TO_MAPPER.put(payloadType.getNumber(), mapper);
    }

    public static ProtoOAPayloadType getPayloadType(GeneratedMessage message) {
        ProtoOAPayloadType payloadType = MESSAGE_TO_PAYLOAD_TYPE.get(message.getClass());
        if (payloadType == null) {
            throw new IllegalArgumentException(
                    "Unknown message type: %s".formatted(message.getClass().getSimpleName()));
        }
        return payloadType;
    }

    public static GeneratedMessage parseMessage(ProtoMessage proto) {
        ProtoMapper<ByteString, ? extends GeneratedMessage> mapper = getMapper(proto.getPayloadType());
        try {
            return mapper.apply(proto.getPayload());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static ProtoMapper<ByteString, ? extends GeneratedMessage> getMapper(
            int payloadTypeNumber) {
        ProtoMapper<ByteString, ? extends GeneratedMessage> mapper =
                PAYLOAD_TYPE_TO_MAPPER.get(payloadTypeNumber);
        if (mapper == null) {
            throw new IllegalArgumentException(
                    "Unknown payload type number: %d".formatted(payloadTypeNumber));
        }
        return mapper;
    }

    @FunctionalInterface
    private interface ProtoMapper<P, R> {
        R apply(P payload) throws InvalidProtocolBufferException;
    }
}
