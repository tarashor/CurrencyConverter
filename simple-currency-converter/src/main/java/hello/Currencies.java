package hello;

import java.util.Collections;
import java.util.Map;

public class Currencies {

    private final String base;
    private final Map<String, Double> rates;

    public Currencies(String base, Map<String, Double> rates) {
        this.base = base;
        this.rates = Collections.unmodifiableMap(rates);
    }


    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}
