package org.knowm.xchange.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.Assert;
import org.knowm.xchange.utils.DateUtils;

/**
 * A class encapsulating the information a "Kline" can contain. Some fields can be empty if not
 * provided by the exchange.
 *
 * <p>A kline contains data representing the latest trade.
 */
@JsonDeserialize(builder = Kline.Builder.class)
public final class Kline implements Serializable {

  private static final long serialVersionUID = -3247730106987193154L;

  private final Instrument instrument;
  private final long id;
  private final BigDecimal open;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal close;
  private final BigDecimal volume;
  private final BigDecimal quoteVolume;
  private final long count;
  /** the timestamp of the kline according to the exchange's server, null if not provided */
  private final Date timestamp;

  /**
   * Constructor
   *
   * @param instrument The tradable identifier (e.g. BTC in BTC/USD)
   * @param open Open price
   * @param high High price
   * @param low Low price
   * @param close Close price
   * @param volume Accumulated volume in base currency
   * @param quoteVolume Accumulated volume in quote currency
   * @param timestamp - the timestamp of the ticker according to the exchange's server, null if not
   *     provided
   */
  private Kline(
      Instrument instrument,
      long id,
      BigDecimal open,
      BigDecimal high,
      BigDecimal low,
      BigDecimal close,
      BigDecimal volume,
      BigDecimal quoteVolume,
      long count,
      Date timestamp) {
    this.instrument = instrument;
    this.id = id;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
    this.quoteVolume = quoteVolume;
    this.count = count;
    this.timestamp = timestamp;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  /**
   * @deprecated CurrencyPair is a subtype of Instrument - this method will throw an exception if
   *     the order was for a derivative
   *     <p>use {@link #getInstrument()} instead
   */
  @Deprecated
  @JsonIgnore
  public CurrencyPair getCurrencyPair() {
    if (instrument == null) {
      return null;
    }
    if (!(instrument instanceof CurrencyPair)) {
      throw new IllegalStateException(
          "The instrument of this order is not a currency pair: " + instrument);
    }
    return (CurrencyPair) instrument;
  }

  public long getId() {
    return id;
  }

  public BigDecimal getOpen() {

    return open;
  }

  public BigDecimal getHigh() {

    return high;
  }

  public BigDecimal getLow() {

    return low;
  }

  public BigDecimal getClose() {
    return close;
  }

  public BigDecimal getVolume() {

    return volume;
  }

  public BigDecimal getQuoteVolume() {
    return quoteVolume;
  }

  public long getCount() {
    return count;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  @Override
  public String toString() {

    return "Kline [instrument="
        + instrument
        + ", id="
        + id
        + ", open="
        + open
        + ", high="
        + high
        + ", low="
        + low
        + ", close="
        + close
        + ", volume="
        + volume
        + ", quoteVolume="
        + quoteVolume
        + ", count="
        + count
        + ", timestamp="
        + DateUtils.toMillisNullSafe(timestamp)
        + "]";
  }

  /**
   * Builder to provide the following to {@link Kline}:
   *
   * <ul>
   *   <li>Provision of fluent chained construction interface
   * </ul>
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Instrument instrument;
    private long id;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private long count;
    private Date timestamp;

    // Prevent repeat builds
    private boolean isBuilt = false;

    public Kline build() {

      validateState();

      Kline kline =
          new Kline(instrument, id, open, high, low, close, volume, quoteVolume, count, timestamp);

      isBuilt = true;

      return kline;
    }

    private void validateState() {

      if (isBuilt) {
        throw new IllegalStateException("The entity has been built");
      }
    }

    public Builder instrument(Instrument instrument) {
      Assert.notNull(instrument, "Null instrument");
      this.instrument = instrument;
      return this;
    }

    /** @deprecated Use {@link #instrument(Instrument)} */
    @Deprecated
    public Builder currencyPair(CurrencyPair currencyPair) {
      return instrument(currencyPair);
    }

    public Builder id(long id) {

      this.id = id;
      return this;
    }

    public Builder open(BigDecimal open) {

      this.open = open;
      return this;
    }

    public Builder high(BigDecimal high) {

      this.high = high;
      return this;
    }

    public Builder low(BigDecimal low) {

      this.low = low;
      return this;
    }

    public Builder close(BigDecimal close) {

      this.close = close;
      return this;
    }

    public Builder volume(BigDecimal volume) {

      this.volume = volume;
      return this;
    }

    public Builder quoteVolume(BigDecimal quoteVolume) {

      this.quoteVolume = quoteVolume;
      return this;
    }

    public Builder count(long count) {

      this.count = count;
      return this;
    }

    public Builder timestamp(Date timestamp) {

      this.timestamp = timestamp;
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kline kline = (Kline) o;
    return Objects.equals(getInstrument(), kline.getInstrument())
        && Objects.equals(getId(), kline.getId())
        && Objects.equals(getOpen(), kline.getOpen())
        && Objects.equals(getHigh(), kline.getHigh())
        && Objects.equals(getLow(), kline.getLow())
        && Objects.equals(getClose(), kline.getClose())
        && Objects.equals(getVolume(), kline.getVolume())
        && Objects.equals(getQuoteVolume(), kline.getQuoteVolume())
        && Objects.equals(getCount(), kline.getCount())
        && Objects.equals(getTimestamp(), kline.getTimestamp());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getInstrument(),
        getId(),
        getOpen(),
        getHigh(),
        getLow(),
        getClose(),
        getVolume(),
        getQuoteVolume(),
        getCount(),
        getTimestamp());
  }
}
