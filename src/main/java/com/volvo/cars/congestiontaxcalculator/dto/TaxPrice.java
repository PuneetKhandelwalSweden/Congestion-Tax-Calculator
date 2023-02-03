package com.volvo.cars.congestiontaxcalculator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "city-based-tax-price")
@Getter
@Setter
public class TaxPrice {

  private String city;
  private List<TimeCalculator> timings;
}