package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrenciesController {


    @RequestMapping("/latest")
    public Currencies latest(@RequestParam(value="base", defaultValue="") String base) {
        ICurrenciesProvider provider = new CustomCurrenciesProvider();
        return provider.getCurrencies(base);
    }
}
