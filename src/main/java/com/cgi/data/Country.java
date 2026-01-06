package com.cgi.data;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class Country {

    String name;

    BigDecimal area;

    BigDecimal inhabitants;

    String capitalCity;

    int neighbours;
}
