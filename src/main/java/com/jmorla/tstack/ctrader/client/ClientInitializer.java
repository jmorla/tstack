package com.jmorla.tstack.ctrader.client;

import com.xtrader.protocol.proto.commons.ProtoMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ClientInitializer extends ChannelInitializer<Channel> {

  private static final Logger log = LoggerFactory.getLogger(ClientInitializer.class);

  private final int idleTimeMillis;
  private final boolean enableSsl;
  private final String host;
  private final int port;
  private final Map<String, CompletableFuture<ProtoMessage>> outgoingRequests;

  public ClientInitializer(
      String host,
      int port,
      boolean enableSsl,
      Map<String, CompletableFuture<ProtoMessage>> outgoingRequests,
      int idleTimeMillis) {
    this.enableSsl = enableSsl;
    this.host = host;
    this.port = port;
    this.outgoingRequests = outgoingRequests;
    this.idleTimeMillis = idleTimeMillis;
  }

  @Override
  protected void initChannel(Channel channel) throws Exception {
    if (enableSsl) {
      log.debug("SSL enabled");
      var sslContext = SslContextBuilder.forClient().build();
      channel.pipeline().addFirst(sslContext.newHandler(channel.alloc(), host, port));
    }
    channel
        .pipeline()
        .addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4))
        .addLast(new ProtobufDecoder(ProtoMessage.getDefaultInstance()))
        .addLast(new LengthFieldPrepender(4))
        .addLast(new ProtobufEncoder())
        .addLast(new CtraderClientHandler(outgoingRequests, idleTimeMillis));
  }
}
