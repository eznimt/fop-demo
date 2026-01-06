package com.cgi.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<Country> loadCountries() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country("Deutschland", new BigDecimal(357600), new BigDecimal(84), "Berlin", 9));
        countryList.add(new Country("Frankreich", new BigDecimal(551700), new BigDecimal(65), "Paris", 8));
        countryList.add(new Country("Italien", new BigDecimal(301300), new BigDecimal(59), "Rom", 6));
        countryList.add(new Country("Spanien", new BigDecimal(505900), new BigDecimal(48), "Madrid", 5));
        return countryList;
    }
}
