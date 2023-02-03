package com.volvo.cars.congestiontaxcalculator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "holidays")
@Getter
@Setter
public class HolidayList {

  private List<HolidayYear> years;
}
