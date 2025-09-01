package com.jmorla.tstack.ctrader;

import com.google.protobuf.GeneratedMessage;
import com.xtrader.protocol.proto.commons.ProtoHeartbeatEvent;
import com.xtrader.protocol.proto.commons.model.ProtoPayloadType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Facade for cTrader API operations providing simplified methods for authentication and messaging.
 */
public final class CtraderApiFacade {

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

  private static ChannelFuture sendEvent(Channel channel, GeneratedMessage proto) {
    return channel.writeAndFlush(proto);
  }
}
