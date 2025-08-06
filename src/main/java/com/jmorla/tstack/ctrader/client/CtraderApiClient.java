package com.jmorla.tstack.ctrader.client;

import com.google.protobuf.GeneratedMessage;
import com.jmorla.tstack.ctrader.mappers.CtraderMapper;
import com.xtrader.protocol.openapi.v2.model.ProtoOAPayloadType;
import com.xtrader.protocol.proto.commons.ProtoMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class CtraderApiClient implements Closeable {

  private static final Logger log = LoggerFactory.getLogger(CtraderApiClient.class);

  private final int port;
  private final String host;
  private final boolean enableSsl;
  private final int idleTimeMillis;
  private final int timeoutMillis;

  private final EventLoopGroup group;
  private final AtomicReference<Channel> channelRef = new AtomicReference<>();

  private final Map<String, CompletableFuture<ProtoMessage>> outgoingRequests =
      new ConcurrentHashMap<>();

  private Bootstrap bootstrap;

  CtraderApiClient(
      int port, String host, boolean enableSsl, int idleTimeMillis, int timeoutMillis) {
    this.port = port;
    this.host = host;
    this.enableSsl = enableSsl;
    this.idleTimeMillis = idleTimeMillis;
    this.timeoutMillis = timeoutMillis;

    group = new NioEventLoopGroup();

    start();
  }

  public static CTraderApiClientBuilder newClient() {
    return new CTraderApiClientBuilder();
  }


  public CompletableFuture<? extends GeneratedMessage> request(GeneratedMessage proto) {
    var type = CtraderMapper.getPayloadType(proto);
    var message = wrap(proto, type);
    return submit(message).thenApply(CtraderMapper::parseMessage);
  }

  private static ProtoMessage wrap(GeneratedMessage proto, ProtoOAPayloadType payloadType) {
    String reqId = UUID.randomUUID().toString();
    log.debug("wrapping proto withPayloadType: {}", payloadType);
    return ProtoMessage.newBuilder()
        .setPayload(proto.toByteString())
        .setPayloadType(payloadType.getNumber())
        .setClientMsgId(reqId)
        .build();
  }

  private CompletableFuture<ProtoMessage> submit(ProtoMessage request) {
    log.debug("request received - PayloadType: {}", request.getPayloadType());

    CompletableFuture<ProtoMessage> requestFuture = new CompletableFuture<>();
    ProtoOAPayloadType payloadType = ProtoOAPayloadType.forNumber(request.getPayloadType());
    if (payloadType == null) {
      requestFuture.completeExceptionally(
          new IllegalArgumentException(
              "Unknown payload type: %d".formatted(request.getPayloadType())));
      return requestFuture;
    }

    Channel channel = channelRef.get();
    if (!channel.isActive()) {
      requestFuture.completeExceptionally(new RuntimeException("Connection unavailable"));
      return requestFuture;
    }

    outgoingRequests.put(request.getClientMsgId(), requestFuture);
    channel.writeAndFlush(request).addListener(v -> log.debug("message successfully written"));

    return requestFuture;
  }

  private synchronized void start() {
    if (bootstrap == null) {
      bootstrap =
          new Bootstrap()
              .group(group)
              .remoteAddress(new InetSocketAddress(host, port))
              .channel(NioSocketChannel.class)
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
              .option(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.TCP_NODELAY, true)
              .handler(
                  new ClientInitializer(host, port, enableSsl, outgoingRequests, idleTimeMillis));
    }

    var future = bootstrap.connect();
    channelRef.set(future.syncUninterruptibly().channel());
  }

  @Override
  public void close() throws IOException {
    if (group != null) {
      group.shutdownGracefully(0, 10, TimeUnit.SECONDS);
    }
  }
}
