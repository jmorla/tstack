package com.jmorla.tstack.ctrader.client;

public class CTraderApiClientBuilder {
  private String host;
  private int port;
  private boolean enableSsl = true;
  private int idleTimeMillis = 30000;
  private int timeoutMillis = 30000;

  public CTraderApiClientBuilder host(String host) {
    this.host = host;
    return this;
  }

  public CTraderApiClientBuilder port(int port) {
    this.port = port;
    return this;
  }

  public CTraderApiClientBuilder enableSsl(boolean enableSsl) {
    this.enableSsl = enableSsl;
    return this;
  }

  public CTraderApiClientBuilder idleTimeMillis(int idleTimeMillis) {
    this.idleTimeMillis = idleTimeMillis;
    return this;
  }

  public CTraderApiClientBuilder timeoutMillis(int timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
    return this;
  }

  public CtraderApiClient create() {
    return new CtraderApiClient(port, host, enableSsl, idleTimeMillis, timeoutMillis);
  }
}
