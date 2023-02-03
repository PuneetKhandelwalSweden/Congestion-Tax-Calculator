package com.volvo.cars.congestiontaxcalculator.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class HolidayMonth {

  List<Integer> dates;
  private int month;
}
