package com.jmorla.tstack.ctrader.client;

import com.google.protobuf.GeneratedMessage;
import com.jmorla.tstack.ctrader.mappers.CtraderMapper;
import com.jmorla.tstack.entities.TimeFrame;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAGetTrendbarsReq;
import com.xtrader.protocol.openapi.v2.ProtoOASymbolByIdReq;
import com.xtrader.protocol.openapi.v2.model.ProtoOAPayloadType;
import com.xtrader.protocol.proto.commons.ProtoHeartbeatEvent;
import com.xtrader.protocol.proto.commons.ProtoMessage;
import com.xtrader.protocol.proto.commons.model.ProtoPayloadType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Facade for cTrader API operations providing simplified methods for authentication and messaging.
 */
public final class CtraderApiFacade {

  /**
   * Sends an application authentication request to cTrader.
   *
   * @param channel the Netty channel to send the request through
   * @param clientId the client ID for authentication
   * @param clientSecret the client secret for authentication
   */
  public static void sendApplicationAuthRequest(
      Channel channel, String clientId, String clientSecret) {
    var req =
        ProtoOAApplicationAuthReq.newBuilder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();

    sendRequest(channel, req, ProtoOAPayloadType.PROTO_OA_APPLICATION_AUTH_REQ);
  }

  /**
   * Sends an account authentication request to cTrader.
   *
   * @param channel the Netty channel to send the request through
   * @param accountId the cTrader account ID
   * @param accessToken the access token for authentication
   */
  public static void sendAccountAuthRequest(Channel channel, int accountId, String accessToken) {
    var req =
        ProtoOAAccountAuthReq.newBuilder()
            .setCtidTraderAccountId(accountId)
            .setAccessToken(accessToken)
            .build();

    sendRequest(channel, req, ProtoOAPayloadType.PROTO_OA_ACCOUNT_AUTH_REQ);
  }

  /**
   * Sends a heartbeat event to maintain connection with cTrader.
   *
   * @param channel the Netty channel to send the heartbeat through
   * @return a ChannelFuture representing the completion of the write operation
   */
  public static ChannelFuture sendHeartbeatEvent(Channel channel) {
    return sendEvent(
        channel,
        ProtoHeartbeatEvent.newBuilder().setPayloadType(ProtoPayloadType.HEARTBEAT_EVENT).build());
  }

  public static void sendBarHistoryRequest(
          Channel channel, int accountId, long symbolId, TimeFrame tf, int lookback, String traceId) {
    var req =
        ProtoOAGetTrendbarsReq.newBuilder()
            .setCtidTraderAccountId(accountId)
            .setSymbolId(symbolId)
            .setPeriod(CtraderMapper.toProtoOATrendbarPeriod(tf))
            .setCount(lookback)
            .build();

    sendTraceableRequest(channel, req, ProtoOAPayloadType.PROTO_OA_GET_TRENDBARS_REQ, traceId);
  }

  public static void sendGetSymbolByIdsRequest(
      Channel channel, Integer accountId, long[] symbolIds, String traceId) {
    var builder = ProtoOASymbolByIdReq.newBuilder();
    builder.setCtidTraderAccountId(accountId);
    for (var id : symbolIds) {
      builder.addSymbolId(id);
    }

    sendTraceableRequest(
        channel, builder.build(), ProtoOAPayloadType.PROTO_OA_SYMBOL_BY_ID_REQ, traceId);
  }

  private static ChannelFuture sendEvent(Channel channel, GeneratedMessage proto) {
    return channel.writeAndFlush(proto);
  }

  public static void sendRequest(
      Channel channel, GeneratedMessage proto, ProtoOAPayloadType payloadType) {
    channel.writeAndFlush(wrap(proto, payloadType, ""));
  }

  public static void sendTraceableRequest(
      Channel channel, GeneratedMessage proto, ProtoOAPayloadType payloadType, String traceId) {
    channel.writeAndFlush(wrap(proto, payloadType, traceId));
  }

  public static void sendRequest(Channel channel, ProtoMessage protoMessage) {
    channel.writeAndFlush(protoMessage);
  }

  private static ProtoMessage wrap(
      GeneratedMessage proto, ProtoOAPayloadType payloadType, String traceId) {
    return ProtoMessage.newBuilder()
        .setClientMsgId(traceId)
        .setPayload(proto.toByteString())
        .setPayloadType(payloadType.getNumber())
        .build();
  }
}
