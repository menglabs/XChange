package info.bitrich.xchangestream.huobi;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.core.StreamingAccountService;
import io.reactivex.Observable;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.exceptions.ExchangeException;

import java.math.BigDecimal;
import java.util.Date;

public class HuobiStreamingAccountService implements StreamingAccountService {

  private final HuobiStreamingService streamingService;

  public HuobiStreamingAccountService(HuobiStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {

    String mode = "2";

    if (args != null && args.length == 1) {
      Object arg0 = args[0];
      if (!(arg0 instanceof Integer) || arg0 == null || (int) arg0 < 0 || (int) arg0 > 2) {
        throw new ExchangeException("Argument 0 must be an Integer in the range [0, 2]!");
      } else {
        mode = String.valueOf((int) arg0);
      }
    }

    String channelName = "accounts.update#" + mode;
    return streamingService
        .subscribeChannel(channelName)
        .map(
            message -> {
              JsonNode data = message.get("data");
              String cur = data.get("currency").textValue();
              BigDecimal balance = data.get("balance").decimalValue();
              BigDecimal available = data.get("available").decimalValue();
              long changeTime = data.get("changeTime").longValue();

              Balance.Builder builder = new Balance.Builder();
              builder.currency(Currency.getInstance(cur));
              builder.total(balance);
              builder.available(available);
              builder.timestamp(new Date(changeTime));
              return builder.build();
            });
  }
}
