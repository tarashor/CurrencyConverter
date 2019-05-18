package hello;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomCurrenciesProvider implements ICurrenciesProvider {
    private final static String DEFAULT_BASE = "EUR";

    @Override
    public Currencies getCurrencies(String base) {
        Map<String, Double> rates = getBaseRates();

        final String baseCurrency = rates.containsKey(base) ? base : DEFAULT_BASE;
        double baseRate = 1/rates.get(baseCurrency);

        Map<String, Double> ratesToBase = new HashMap<>();
        rates.forEach((cur, rate) ->{
            if (!cur.equals(baseCurrency)){
                ratesToBase.put(cur, rate * baseRate);
            }
        });


        return new Currencies(baseCurrency, ratesToBase);
    }

    public static Map<String, Double> getBaseRates() {
        Map<String, Double> baseRates = new HashMap<>();

        baseRates.put("UAH", 29.423548);
        baseRates.put("RUB", 72.288964);
        baseRates.put("USD", 1.116345);
        baseRates.put("PLN", 4.306022);
        baseRates.put("UGX", 4194.000621);
        baseRates.put(DEFAULT_BASE, 1.0);
        noise(baseRates);

        return baseRates;
    }

    private static void noise(Map<String, Double> map) {
        Random random = new Random();
        for (String key : map.keySet()) {
            map.put(key, (random.nextDouble() * 0.1 + 1) * map.get(key));
        }
    }
}
