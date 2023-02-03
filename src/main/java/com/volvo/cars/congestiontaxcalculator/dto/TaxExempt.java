package com.volvo.cars.congestiontaxcalculator.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tax-exempt")
@Getter
@Setter
public class TaxExempt {

  private Set<String> vehicles;

}
