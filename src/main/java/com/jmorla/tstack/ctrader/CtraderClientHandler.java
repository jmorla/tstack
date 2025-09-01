package com.jmorla.tstack.ctrader;

import com.google.protobuf.GeneratedMessage;
import com.xtrader.protocol.openapi.v2.ProtoOAErrorRes;
import com.xtrader.protocol.openapi.v2.model.ProtoOAPayloadType;
import com.xtrader.protocol.proto.commons.ProtoHeartbeatEvent;
import com.xtrader.protocol.proto.commons.ProtoMessage;
import com.xtrader.protocol.proto.commons.model.ProtoPayloadType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CtraderClientHandler extends SimpleChannelInboundHandler<ProtoMessage> {

  private static final Logger log = LoggerFactory.getLogger(CtraderClientHandler.class);

  private final Map<String, CompletableFuture<ProtoMessage>> outgoingRequests;
  private final int idleTimeMillis;

  public CtraderClientHandler(
      Map<String, CompletableFuture<ProtoMessage>> outgoingRequests, int idleTimeMillis) {
    this.outgoingRequests = outgoingRequests;
    this.idleTimeMillis = idleTimeMillis;
  }

  @Override
  protected void channelRead0(
      ChannelHandlerContext channelHandlerContext, ProtoMessage protoMessage) throws Exception {
    String reqId = protoMessage.getClientMsgId();
    var future = outgoingRequests.get(reqId);

    if (protoMessage.getPayloadType() == ProtoOAPayloadType.PROTO_OA_ERROR_RES_VALUE) {
      var error = ProtoOAErrorRes.parseFrom(protoMessage.getPayload());
      future.completeExceptionally(
          new RuntimeException("%s : %s".formatted(error.getErrorCode(), error.getDescription())));
    }
    if (protoMessage.getPayloadType() == ProtoOAPayloadType.PROTO_OA_ACCOUNT_AUTH_RES_VALUE) {
      configureHeartBeatEventWorker(channelHandlerContext);
    }

    if(protoMessage.getPayloadType() == ProtoPayloadType.HEARTBEAT_EVENT_VALUE) {
      return;
    }

    if(future == null) {
      log.debug("No future to complete for this proto message type :: {}", protoMessage.getPayloadType());
      return;
    }

    future.complete(protoMessage);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.error("Exception caught in handler", cause);
  }

  private void configureHeartBeatEventWorker(ChannelHandlerContext ctx) {
    ctx.executor()
        .scheduleAtFixedRate(
            () -> {
              final var channel = ctx.channel();
              if (channel.isActive()) {
                sendHeartbeatEvent(channel)
                    .addListener(e -> log.debug("Heartbeat sent..."));
              }
            },
            idleTimeMillis,
            idleTimeMillis,
            TimeUnit.MILLISECONDS);
  }

  /**
   * Sends a heartbeat event to maintain connection with cTrader.
   *
   * @param channel the Netty channel to send the heartbeat through
   * @return a ChannelFuture representing the completion of the write operation
   */
  private ChannelFuture sendHeartbeatEvent(Channel channel) {
    return sendEvent(
        channel,
        ProtoHeartbeatEvent.newBuilder().setPayloadType(ProtoPayloadType.HEARTBEAT_EVENT).build());
  }

  private ChannelFuture sendEvent(Channel channel, GeneratedMessage proto) {
    return channel.writeAndFlush(proto);
  }
}
