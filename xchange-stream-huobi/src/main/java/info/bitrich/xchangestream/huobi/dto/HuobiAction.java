package info.bitrich.xchangestream.huobi.dto;

public enum HuobiAction {
  SUB("sub"),
  REQ("req"),
  PING("ping"),
  PONG("pong"),
  PUSH("push");

  HuobiAction(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }
}
