package info.bitrich.xchangestream.huobi;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.ConnectionStateModel.State;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.knowm.xchange.huobi.HuobiExchange;

public class HuobiStreamingExchange extends HuobiExchange implements StreamingExchange {

  private static final String API_BASE_URI = "wss://api.huobi.pro/ws";
  private static final String API_URI_AWS = "wss://api-aws.huobi.pro/ws";

  private HuobiStreamingService streamingService;
  private HuobiStreamingMarketDataService streamingMarketDataService;
  private HuobiStreamingAccountService streamingAccountService;

  @Override
  protected void initServices() {
    super.initServices();
    Boolean aws =
        (Boolean)
            getExchangeSpecification()
                .getExchangeSpecificParameters()
                .getOrDefault("AWS", Boolean.FALSE);
    this.streamingService = new HuobiStreamingService(aws ? API_URI_AWS : API_BASE_URI);
    this.streamingService.useCompressedMessages(true);
    streamingMarketDataService = new HuobiStreamingMarketDataService(streamingService);
    streamingAccountService = new HuobiStreamingAccountService(streamingService);
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    return streamingService.connect();
  }

  @Override
  public Completable disconnect() {
    return streamingService.disconnect();
  }

  @Override
  public boolean isAlive() {
    return streamingService.isSocketOpen();
  }

  @Override
  public Observable<Throwable> reconnectFailure() {
    return streamingService.subscribeReconnectFailure();
  }

  @Override
  public Observable<Object> connectionSuccess() {
    return streamingService.subscribeConnectionSuccess();
  }

  @Override
  public Observable<State> connectionStateObservable() {
    return streamingService.subscribeConnectionState();
  }

  @Override
  public StreamingMarketDataService getStreamingMarketDataService() {
    return streamingMarketDataService;
  }

  public StreamingAccountService getStreamingAccountService() {
    return streamingAccountService;
  }

  @Override
  public void useCompressedMessages(boolean compressedMessages) {
    streamingService.useCompressedMessages(compressedMessages);
  }
}
